var searchInput = document.getElementById('search');
var searchIcon = document.getElementById('search-icon');
var magnifierIcon = searchIcon.querySelector('.magnifier');
var crossIcon = searchIcon.querySelector('.cross');

// Update icons based on input content
function updateIcons() {
  if (searchInput.value.trim() === '') {
    magnifierIcon.classList.remove('hidden');
    crossIcon.classList.add('hidden');
  } else {
    magnifierIcon.classList.add('hidden');
    crossIcon.classList.remove('hidden');
  }
}

function updateQuery() {
  // Get the search query
  var searchQuery = searchInput.value.toLowerCase();
  
  // Get all elements with the class 'filtrable'
  var items = document.querySelectorAll('.filtrable');

  // Loop through all filtrable items
  items.forEach(item => {
    // Check if the item text includes the search query
    if (item.textContent.toLowerCase().includes(searchQuery)) {
        // If it includes, remove 'hidden' class
        item.classList.remove('hidden');
        item.querySelectorAll('.filtrable').forEach(child => {
            if(!child.classList.contains("category")) child.classList.add('parent-passed');
        });
    } else {
        // If it doesn't include, add 'hidden' class
        item.classList.add('hidden');
        // Remove 'parent-passed' class from children
        item.querySelectorAll('.filtrable').forEach(child => {
            child.classList.remove('parent-passed');
        });
    }
  });

  // Additional logic to ensure children of a "passed" parent are shown
  items.forEach(item => {
    if (!item.textContent.toLowerCase().includes(searchQuery) && !item.classList.contains('parent-passed')) {
      item.classList.add('hidden');
    } else {
      item.classList.remove('hidden');
    }
  });
}

// Event listener for input field changes
let timeout;
searchInput.addEventListener('input', () => {
  updateIcons();
  clearTimeout(timeout);
  timeout = setTimeout(() => {
    updateQuery();
    timeout = undefined;
    document.documentElement.scrollTo({
      top: document.getElementById("grids").getBoundingClientRect().top + scrollY - 100
    });
  }, 300);
});

searchIcon.addEventListener('click', function() {
    searchInput.focus();
    if (searchInput.value.trim() != '') {
        searchInput.value = '';
        updateQuery();
        updateIcons();
    }
});

// Initial check to set the correct icon state
updateIcons();
