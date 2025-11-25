



// This is to run only after the html is ready
document.addEventListener("DOMContentLoaded",()=>{
    console.log("BookBites front end load");

    fetchBooks()
});

async function fetchBooks() {
    const response = await fetch("/api/books"); //ask backend for books
    const books = await response.json();        //turn JSON into JS objects

    console.log("Books from backend:", books);
}