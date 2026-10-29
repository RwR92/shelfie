// Dev mode (IntelliJ's server) calls the backend directly; behind Nginx we use the same origin
const API_URL = location.port === "63342" ? "http://localhost:8080/api" : "/api";
async function fetchBooks(){
    const response = await fetch (`${API_URL}/books`);

    if(!response.ok){
        throw new Error(`Server answered ${response.status}`);
    }

    return response.json();
}

const statusEl = document.querySelector("#status");
const listEl = document.querySelector("#book-list");

function renderBooks(books) {
    listEl.innerHTML = "";

    if (books.length === 0) {
        statusEl.textContent = "No books yet.";
        return;
    }

    for (const book of books) {
        const li = document.createElement("li");
        li.classList.add("list-group-item");

        const title = document.createElement("strong");
        title.textContent = book.title;

        const meta = document.createElement("span");
        meta.textContent = ` by ${book.author} (${book.publishYear ?? "unknown year"})`;

        li.append(title, meta);
        listEl.append(li);
    }

    statusEl.textContent = `${books.length} books`;
}

async function init() {
    try {
        const books = await fetchBooks();
        renderBooks(books);
    } catch (error) {
        statusEl.textContent = "Could not load books. Is the backend running?";
        console.error(error);
    }
}

init();