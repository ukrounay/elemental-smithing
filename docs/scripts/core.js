const sidebar = document.getElementById("sidebar");
const toggleButton = document.getElementById("sidebar-toggle");
const sidebarLinks = document.querySelectorAll('.sidebar nav a');
const sections = document.querySelectorAll('main section');
const topButton = document.getElementById("top-button"); 

// Toggle sidebar visibility on small screens
toggleButton.addEventListener("click", () => {
    toggleButton.classList.toggle("active");
    sidebar.classList.toggle("open");
    if (sidebar.classList.contains("open")) lockScroll(); 
    else unlockScroll();
});

function checkTopButtonVisibility() {
    topButton.style.display = 
        scrollY > 100 
        && !sidebar.classList.contains("open") 
        && popup.classList.contains("hidden") 
        ? "block" 
        : "none";
}

// Function to remove active class from all links
function removeActiveClasses() {
    sidebarLinks.forEach(link => link.classList.remove('active'));
}

// Function to add active class to the link corresponding to the current section
function addActiveClass(currentSection) {
    sidebarLinks.forEach(link => {
        if (link.getAttribute('href').substring(1) == currentSection) {
            link.classList.add('active');
        }
    });
}

// Event listener for scroll event to highlight current section link
window.addEventListener('scroll', () => {
    let current = "";
    sections.forEach(section => {
        let sectionTop = section.offsetTop;
        if (scrollY >= sectionTop
         || scrollY + screen.height >= sectionTop + section.getBoundingClientRect().height) {
            current = section.getAttribute('id');
        }
    });
    removeActiveClasses();
    addActiveClass(current);

    checkTopButtonVisibility()
});

// Event listeners for sidebar links to smoothly scroll to sections
sidebarLinks.forEach(link => {
    link.addEventListener("click", (event) => {
        event.preventDefault();
        let id = link.getAttribute("href").substring(1);
        let targetElem = document.getElementById(id);
        document.documentElement.scrollTo({
            top: targetElem.getBoundingClientRect().top + scrollY - 150,
            behavior: "smooth"
        });

        // Close sidebar and popup after click
        toggleButton.classList.remove("active");
        sidebar.classList.remove("open");
        document.getElementById("popup").classList.add("hidden");
        unlockScroll();
    });
});
