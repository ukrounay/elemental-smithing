const colorMap = {
	black: { hex: '#000000', shadow: '#202020' },
	dark_blue: { hex: '#0000AA', shadow: '#000050' },
	dark_green: { hex: '#00AA00', shadow: '#005000' },
	dark_aqua: { hex: '#00AAAA', shadow: '#005050' },
	dark_red: { hex: '#AA0000', shadow: '#500000' },
	dark_purple: { hex: '#AA00AA', shadow: '#500050' },
	gold: { hex: '#FFAA00', shadow: '#805000' },
	gray: { hex: '#AAAAAA', shadow: '#555555' },
	dark_gray: { hex: '#555555', shadow: '#282828' },
	blue: { hex: '#5555FF', shadow: '#2828AA' },
	green: { hex: '#55FF55', shadow: '#28AA28' },
	aqua: { hex: '#55FFFF', shadow: '#28AAAA' },
	red: { hex: '#FF5555', shadow: '#AA2828' },
	light_purple: { hex: '#FF55FF', shadow: '#AA28AA' },
	yellow: { hex: '#FFFF55', shadow: '#AAAA28' },
	white: { hex: '#FFFFFF', shadow: '#A0A0A0' }
};

function loadBookContents(filename, container) {
	fetch(`data/${filename}.json`)
		.then(response => response.json())
		.then(bookData => {
			// Container for the book content
			let bookContainer = document.createElement('div');
			bookContainer.classList = 'flex-vertical gap-1';

			// Create header
			let header = document.createElement("h3");
			header.classList.add("translatable");
			header.dataset.translate = "popup.description.book.header"; 
			header.innerHTML = translations.popup.description.book.header;
			bookContainer.appendChild(header);

			// Create book title element
			let title = document.createElement("p");

			let titleKey = document.createElement('span');
			titleKey.classList.add("translatable");
			titleKey.dataset.translate = "popup.description.book.title"; 
			titleKey.innerHTML = translations.popup.description.book.title;

			let titleElement = document.createElement('span');
			readWithStyles(titleElement, bookData.title)

			title.appendChild(titleKey);
			title.append(": ");
			title.appendChild(titleElement);
			
			bookContainer.appendChild(title);

			// Create book author element
			let author = document.createElement("p");

			let authorKey = document.createElement('span');
			authorKey.classList.add("translatable");
			authorKey.dataset.translate = "popup.description.book.author"; 
			authorKey.innerHTML = translations.popup.description.book.author;

			let authorElement = document.createElement('span');
			readWithStyles(authorElement, bookData.author)

			author.appendChild(authorKey);
			author.append(": ");
			author.appendChild(authorElement);
			
			bookContainer.appendChild(author);


			// Create book pages elements
			bookData.pages.forEach(pageContent => {
				let pageElement = document.createElement('p');
				pageElement.className = 'book-page';
		
				// Each page can have multiple text elements
				pageContent.forEach(content => {
					// Check if the content is an object (styled text) or a simple string
					let spanElement = document.createElement('span');
					readWithStyles(spanElement, content);
					pageElement.appendChild(spanElement);

				});

				bookContainer.appendChild(pageElement);
			});

		container.appendChild(bookContainer);
	});
}

function readWithStyles(element, content) {
	if (typeof content === 'object' && content.text) {
		element.innerText = content.text.replace(/\n{2,}/g, '\n');;

		// Apply styles if present
		if (content.color) element.style.color = colorMap[content.color].hex;
		if (content.bold) element.style.fontWeight = 'bold';
		if (content.italic) element.style.fontStyle = 'italic';
		if (content.shadow) element.style.textShadow = `0.1rem 0.1rem 0 ${colorMap[content.color ? content.color : black].shadow}`;

	} else if (typeof content === 'string') {
		// Handle simple string content
		element.innerText = content;
	}
}
