const root = document.documentElement;
const popup = document.getElementById("popup");
const langToggles = document.querySelectorAll(".lang-toggle");
const toggleGroups = document.querySelectorAll(".toggle-group");
let translations;
let materials;
let content;
let currentLang = localStorage.getItem("language") || "en";

// Function to load and apply translations
function applyTranslations(lang) {
    fetch(`data/translations_${lang}.json`)
        .then(response => response.json())
        .then(data => {
            translations = data;
            document.querySelectorAll(".translatable").forEach(element => {
                const translateKey = element.getAttribute("data-translate");
                const keys = translateKey.split('.');
                let translatedText = data;

                // Access nested translation keys
                keys.forEach(key => {
                    translatedText = translatedText[key];
                });

                element.innerHTML = translatedText;
            });
        });
}

// Function to load dynamic content
function loadContent(lang, type, containerId) {
    fetch(`data/${type}_${lang}.json`)
        .then(response => response.json())
        .then(data => {
            content = data;
            const container = document.getElementById(containerId);
            container.innerHTML = ""; // Clear previous content
            data.categories.forEach(category => {
                const categoryDiv = document.createElement("div");
                categoryDiv.classList = "filtrable category";
                if(category.name) categoryDiv.innerHTML += `<h3>${category.name}</h3>`;

                if(category.description) {
                    let quote = document.createElement("blockquote");
                    quote.classList = "info";
                    quote.innerHTML += `
                        <svg class="svg-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 90 90"><rect class="outer" x="40" y="20" width="10" height="10"/><path class="outer" d="M90,90H0V0H90ZM80,10H10V80H80Z"/><rect class="outer" x="40" y="40" width="10" height="30"/></svg>
                    `;
                    let quoteText = document.createElement("div");
                    quoteText.innerHTML = category.description;
                    quote.appendChild(quoteText);
                    categoryDiv.appendChild(quote);
                }

                const categoryGrid = document.createElement("div");
                categoryGrid.classList.add("content-grid")
                category[type].forEach(element => {
                    const elem = document.createElement("div");
                    elem.classList = "element filtrable";
                    elem.innerHTML = `
                        <div class="ratio c1-1">
                            <img src="images/${type}/${element.id}.png" alt="${element.name}" class="pixelated ratio-item">
                        </div>
                        <p>${element.name}</p>
                    `;
                    // Add event listener for item details (if needed)
                    elem.addEventListener("click", () => {
                        showPopup(element, type);
                    });
                    categoryGrid.appendChild(elem);
                });

                categoryDiv.appendChild(categoryGrid);
                container.appendChild(categoryDiv);
            });
        });

}

function loadMaterials(lang) {
    fetch(`data/materials_${lang}.json`)
        .then(response => response.json())
        .then(data => materials = data);
}



const propertyContainer = popup.querySelector(".properties");
const propertyContent = popup.querySelector("#popup-element-properties");

const recipeContainer = popup.querySelector(".recipe");
const recipeContent = popup.querySelector("#popup-element-recipe");

const descriptionContent = popup.querySelector("#popup-element-description");

const galleryContainer = popup.querySelector(".gallery");
const galleryContent = popup.querySelector("#popup-element-gallery");

const otherContent = popup.querySelector(".other");

// Function to show item popup
function showPopup(item, type) {
    popup.querySelector("#popup-element-icon").src = `images/${type}/${item.id}.png`;
    popup.querySelector("#popup-element-name").textContent = item.name;


    // properties

    propertyContent.innerHTML = "";
    propertyContainer.classList.add("hidden")

    item.properties.forEach(prop => 
        propertyContent.innerHTML += `<tr><td class="property">${prop.name}</td><td class="property-value">${prop.value}</td></tr>`
    );
    if(item.material) {
        propertyContent.innerHTML += `<tr><td class="property">${translations.popup.properties.material}</td><td class="property-value">${materials[item.material].name}</td></tr>`
        if(item.material) materials[item.material].properties.forEach(prop => 
            propertyContent.innerHTML += `<tr><td class="property">${prop.name}</td><td class="property-value">${prop.value}</td></tr>`
        );
    }

    if(propertyContent.childElementCount > 0) 
        propertyContainer.classList.remove("hidden");


    // recipe

    recipeContent.innerHTML = "";
    recipeContainer.classList.add("hidden");

    if(item.recipe) {
        recipeContent.innerHTML 
            = item.recipe == "image" 
                ? `<img src="images/crafts/${item.id}.png">`
                : item.recipe;
    } 
    
    if(recipeContent.childElementCount > 0) 
        recipeContainer.classList.remove("hidden");


    // description

    descriptionContent.innerHTML = "";
    descriptionContent.classList.add("hidden");

    if(item.description) {
        let quote = document.createElement("blockquote");
        quote.classList = "info";
        quote.innerHTML += `
            <svg class="svg-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 90 90"><rect class="outer" x="40" y="20" width="10" height="10"/><path class="outer" d="M90,90H0V0H90ZM80,10H10V80H80Z"/><rect class="outer" x="40" y="40" width="10" height="30"/></svg>
        `;
        let quoteText = document.createElement("div");
        quoteText.innerHTML = item.description;
        quote.appendChild(quoteText);
        
        descriptionContent.appendChild(quote);
    }

    if(descriptionContent.childElementCount > 0) 
        descriptionContent.classList.remove("hidden");


    // gallery
    
    galleryContent.innerHTML = "";
    galleryContainer.classList.add("hidden");

    if(item.gallery) {
        if (item.gallery.length == 1) {
            let source = item.gallery[0];
            galleryContent.innerHTML = `<div class="ratio c16-9"><img class="ratio-item" style="width: 100%" src="images/gallery/${source.filename ? source.filename : source}"></div>`;
        } else {
            let slider = document.createElement("div");
            slider.classList = "nemeo-slider";
            slider.dataset.sources = item.gallery.map(source => `${source.type ? source.type : "image"}:images/gallery/${source.filename ? source.filename : source}`).join(",");
            galleryContent.appendChild(slider);
            initializeSlider(galleryContent.querySelector(".nemeo-slider"));
        }
    }

    if(galleryContent.childElementCount > 0) 
        galleryContainer.classList.remove("hidden");


    // other content

    otherContent.innerHTML = "";
    if(item.bookContents) 
        loadBookContents(item.bookContents, otherContent);
    if(item.sound) {
        let player = document.createElement("audio");
        let source = document.createElement("source");
        source.src = `sounds/${item.sound}`;
        let pieces = item.sound.split(".");
        source.type = `audio/${pieces[pieces.length - 1]}`
        player.controls = true;
        player.appendChild(source);
        otherContent.appendChild(player); 
    }
    if(item.video) {
        let player = document.createElement("video");
        let source = document.createElement("source");
        source.src = `videos/${item.video}`;
        let pieces = item.video.split(".");
        source.type = `video/${pieces[pieces.length - 1]}`
        player.controls = true;
        player.preload = "metadata";
        player.appendChild(source);
        otherContent.appendChild(player); 
    }


    popup.classList.remove("hidden");
    lockScroll();
    let sidebarY = sidebar.getBoundingClientRect().top + scrollY;
    if(scrollY < sidebarY) root.scrollTo({top: sidebarY});
}


// Close popup
document.getElementById("popup-close").addEventListener("click", () => {
    closePopup();
});


function closePopup() {
    document.getElementById("popup").classList.add("hidden");
    unlockScroll();
}


// Function to handle language change
langToggles.forEach(toggle => {
    toggle.addEventListener("click", (e) => {
        const selectedLang = toggle.getAttribute("data-lang");
        currentLang = selectedLang;
        localStorage.setItem("language", selectedLang);
        applyTranslations(currentLang);
        loadContent(currentLang, 'items', 'items-content');
        loadContent(currentLang, 'blocks', 'blocks-content');
        loadContent(currentLang, 'effects', 'effects-content');
    });
});

function lockScroll() {
    document.body.style.overflow = "hidden";
    checkTopButtonVisibility()
}


function unlockScroll() {
    document.body.style.overflow = "auto";
    checkTopButtonVisibility()
}

// Initial load of translations and dynamic content
applyTranslations(currentLang);
loadMaterials(currentLang);
loadContent(currentLang, 'items', 'items-content');
loadContent(currentLang, 'blocks', 'blocks-content');
loadContent(currentLang, 'effects', 'effects-content');

toggleGroups.forEach(group => group.querySelectorAll(".toggle-group-option").forEach(option => {
    option.addEventListener("click", () => {
        document.getElementById(option.dataset.target).dataset.mode = option.dataset.mode;
        document.getElementById(option.dataset.target).scrollIntoView({behavior: "smooth"});
        group.querySelectorAll(".toggle-group-option").forEach(op => op.classList.remove("active"));
        option.classList.add("active");
    });
}));