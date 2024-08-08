let themeButtons = document.getElementsByClassName("theme-toggle");
let themeLabels = document.getElementsByClassName('theme-label');

let currentTheme = localStorage.getItem("theme");
if (currentTheme == undefined) currentTheme = "auto";

setTheme(currentTheme);

function setTheme(theme) {
    localStorage.setItem("theme", theme);
	document.body.dataset.theme = theme;
    currentTheme = theme;
    for (const themeButton of themeButtons) {
        themeButton.classList = theme + " switch theme-toggle";
        themeButton.title = theme;
    }
    for (const themeLabel of themeLabels) {
        themeLabel.innerText = theme;
    }
}
function toggleTheme() {
    setTheme(currentTheme == 'light' ? "dark" : currentTheme == 'dark' ? "auto" : "light")
}