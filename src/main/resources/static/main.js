const API_BASE = "http://localhost:8080/api";
let currentBookId = null; //last clicked book here
let currentBookTitle ="";// just for the heading text
let selectedPromptId = null; 

// This is to run only after the html is ready
document.addEventListener("DOMContentLoaded",()=>{
    console.log("BookBites front end load");
    // load books when the page is ready
    fetchBooks()
    setupNewBookForm();
    setupNewPostForm();
    setupFullscreenMode();
});

async function fetchBooks() {
    const response = await fetch(`${API_BASE}/books`); //ask backend for books
    const books = await response.json();        //turn JSON into JS objects

    console.log("Books from backend:", books);
    renderBookList(books);
}

function renderBookList(books){
    // Find the empty "books-container"
    const container = document.getElementById("books-container");
    // const element = container.querySelector("ul") || document.createElement("ul");

    if(!container){
        console.error("No #book-container element found");
        return;
    }
    // Clear anything that was there
    container.innerHTML = "";

    const element = document.createElement("div");

    // For each book, create a clickable element
    books.forEach((book)=>{
        const item = document.createElement("button");
        item.className = "book-item";
        item.textContent = book.title;
        console.log(book.title);

    // use this to load posts for that book
        item.addEventListener("click", ()=>{
            // remember which book is selected
            currentBookId = book.id;
            currentBookTitle = book.title;
            console.log("Clicked book:", book);

            // highlight this book in the list
            document
                .querySelectorAll(".book-item")
                .forEach((btn) => btn.classList.remove("selected-book"));
            item.classList.add("selected-book");

            // load that book's posts from the backend
            fetchPostsForBook(book.id);
            // load prompts for this book
            loadPromptsForBook(book.id);
        });
        element.appendChild(item);
    });
    container.appendChild(element);
    // if(!container.querySelector("ul")){
    //     container.appendChild(item);
    // }
}

function renderPostsList(posts){
    const heading = document.getElementById("posts-heading");
    const container = document.getElementById("posts-container");

    if(!container){
        console.error("No #posts-container element found");
        return;
    }

    //update the heading to show which book we're on
    if(currentBookTitle){
        heading.textContent = `Reflections on: ${currentBookTitle}`;
    }else{
        heading.textContent = "Reflections";
    }

    // Clear out any old posts
    container.innerHTML = "";

    // If there are no posts, show a friendly message
    if(!posts || posts.length === 0){
        const empty = document.createElement("p");
        empty.textContent = "No posts yet. Be the first to write about this book!";
        container.appendChild(empty);
        return;
    }

    // post cards
    posts.forEach((post) =>{
        const article = document.createElement("article");
        article.className = "post-card";

        const headline = document.createElement("h3");
        headline.textContent = post.headline;

        const divide = document.createElement("div");
        divide.className = "post-meta";
        divide.textContent = `Written by ${post.authorName} • Impact: ${post.rating}/5`;

        const writing = document.createElement("p");
        writing.innerHTML = post.content;

        article.append(headline,divide,writing);
        container.appendChild(article);
    })
}

function selectPrompt(promptId, promptText){
    selectedPromptId = promptId;

    const selectedTextEl = document.getElementById("selected-prompt-text");
    if(selectedTextEl){
        selectedTextEl.textContent = promptText;
    }

    const buttons = document.querySelectorAll(".prompt-button");
    buttons.forEach((btn) => {
        btn.classList.removed("active-prompt");
    });
    const promptListEl = document.getElementById("prompt-list");
    if(promptListEl){
        [...promptListEl.children].forEach((btn) => {
            if(btn.textContent === promptText){
                btn.classList.add("active-prompt");
            }
        })
    }
}

function setupNewPostForm(){
    const form = document.getElementById("new-post-form");
    const message = document.getElementById("form-message");

    if(!form){console.warn("No new-post-form found in HTML"); return;
    }

    form.addEventListener("submit", async(event) =>{
        event.preventDefault(); //dont let browser reload the page

        //clear old message
        message.textContent = "";
        message.className = "form-message";

        //must have selected a book on the left
        if(!currentBookId){
            message.textContent = "Pick a book on the left first.";
            message.classList.add("error");
            return;
        }

        // grab values from inputs
        const authorName = document.getElementById("authorName").value.trim();
        const headline = document.getElementById("headline").value.trim();
        const rating = Number(document.getElementById("rating").value);
        const editor = tinymce.get('content');
        const content = editor ? editor.getContent().trim(): '';

        // simple validation
        if(!authorName|| !headline || !content){
            message.textContent = "Please fill in your name, a headline, and your thoughts.";
            message.classList.add("error");
            return;
        }

        const newPost ={
            authorName: authorName,
            headline: headline,
            rating: rating,
            content: content,
            promptId: selectedPromptId
        };

        try {
            const response = await fetch(`${API_BASE}/books/${currentBookId}/posts`,{
                method:"POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body:JSON.stringify(newPost)
            });

            if(!response.ok){
                console.error("Failed to save post. Status:", response.status);
                message.textContent = "Could not save post. Try again.";
                message.classList.add("error");
                return;
            }

            const savedPost = await response.json();
            console.log("Saved post:", savedPost);
            
            message.textContent = "Reflection saved!";
            message.classList.add("success");

            //reload posts for this book so the new one appears
            fetchPostsForBook(currentBookId);

            // clear form
            form.reset();
            document.getElementById("rating").value = "5";

            const editor = tinymce.get('content');
            if(editor) {
                editor.setContent('');
            }

            selectedPromptId = null;
            const selectedTextEl = document.getElementById("selected-prompt-text");
            if (selectedTextEl) {
                selectedTextEl.textContent = "";
            }
            const buttons = document.querySelectorAll(".prompt-button";
                buttons.forEach((btn) => btn.classList.remove("active-prompt"));
            )

        } catch (error) {
            console.error("Error saving posts:", error);
            message.textContent = "Network error. Please try again.";
            message.classList.add("error");
        }
    });
}

async function fetchPostsForBook(bookId){
    try {
        const response = await fetch(`${API_BASE}/books/${bookId}/posts`);

        if(!response.ok){
            console.error("Failed to load posts. Status:", response.status);
            renderPostsList([]);  // show 'no posts' message
            return;
        }
        const posts = await response.json();
        console.log("Posts from backend for book", bookId, posts);
        renderPostsList(posts);
    } catch (error) {
      console.error("Error calling posts API:", error);
      renderPostsList([]);  
    }
}

async function loadPromptsForBook(bookId) {
    const promptListEl = document.getElementById("prompt-list");
    const selectedTextEl = document.getElementById("selected-prompt-text");

    if (!promptListEl) {
        console.warn("No #prompt-list element found in HTML");
        return;
    }

    promptListEl.innerHTML = "";
    if (selectedTextEl) {
        selectedTextEl.textContent = "";
    }
    selectedPromptId = null;

    try {
        const response = await fetch(`${API_BASE}/books/${bookId}/prompts`);

        if(!response.ok){
            console.error("Failed to load prompts. Status:", response.status);
            promptListEl.textContent = "Could not load prompts.";
            return;
        }

        const prompts = await response.json();
        console.log("Prompts from backend for book", bookId, prompts);
        
        if (!prompts || prompts.length === 0) {
            promptListEl.textContent = "No prompts yet. Just write freely!";
            return;
        }

        prompts.forEach((prompt) => {
            const btn = document.createElement("button");
            btn.type = "button";
            btn.className = "prompt-button";
            btn.textContent = prompt.text;

            btn.addEventListener("click", () =>{
                selectPrompt(prompt.id, prompt.text);
            });

            promptListEl.appendChild(btn);
        });
    }catch (error) {
        console.error("Error calling prompts API:", error);
        promptListEl.textContent = "Error loading prompts.";
        
    }
}

function setupNewBookForm(){
    const form = document.getElementById("new-book-form");
    const message = document.getElementById("book-form-message")

    if(!form){
        console.warn("No new-book-form found in HTML");
        return;
    }

    form.addEventListener("submit", async (event) =>{
        event.preventDefault();

        message.textContent = "";
        message.className = "form-message";

        const titleInput = document.getElementById("newBookTitle");
        const authorInput = document.getElementById("newBookAuthor");

        const title = titleInput.value.trim();
        const author = authorInput.value.trim();
        
        if(!title){
            message.textContent = "Please enter a book title.";
            message.classList.add("error");
            return;
        }

        const newBook = {
            title: title,
            author: author || null
        };

        try{
            const response = await fetch(`${API_BASE}/books`,{
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(newBook)
            });

            if (!response.ok){
                console.error("Failed to save book. Status:", response.status);
                message.textContent = "Could not save book. Try again";
                message.classList.add("error");
                return;
            }

            const saveBook = await response.json();
            console.log("Saved book:", saveBook);

            message.textContent = "Book added!";
            message.classList.add("success");

            // refreshes the list on the left
            fetchBooks();

            // clear the form
            form.reset();
        }catch(error){
            console.error("Error saving book:", error);
            message.textContent = "Network error. Please try again.";
            message.classList.add("error");
        }
    });
}

function setupFullscreenMode(){
    const openBtn = document.getElementById("fullscreen-btn");
    const overlay = document.getElementById("fullscreen-overlay");
    const exitBtn = document.getElementById("exit-fullscreen");
    const saveBtn = document.getElementById("save-fullscreen");
    const editor = document.getElementById("fullscreen-textarea");
    const regularTextarea = document.getElementById("content");

    // always starts hidden
    overlay.classList.add("hidden")

    // Open fullscreen mode
    openBtn.addEventListener("click", ()=>{
        editor.value = regularTextarea.value;
        overlay.classList.remove("hidden");
    });

    // Exit without saving
    exitBtn.addEventListener("click", ()=>{
        overlay.classList.add("hidden");
    });

    // Save and exit
    saveBtn.addEventListener("click", ()=>{
        regularTextarea.value = editor.value;
        overlay.classList.add("hidden");
    });
}

tinymce.init({
    selector: "#content",
    plugins: "powerpaste casechange searchreplace autolink directionality advcode visualblocks visualchars image link media mediaembed codesample table charmap pagebreak nonbreaking anchor tableofcontents insertdatetime advlist lists checklist wordcount tinymcespellchecker editimage help formatpainter permanentpen charmap linkchecker emoticons advtable export print autosave",
    toolbar: "undo redo print spellcheckdialog formatpainter | blocks fontfamily fontsize | bold italic underline forecolor backcolor | link image | alignleft aligncenter alignright alignjustify lineheight | checklist bullist numlist indent outdent | removeformat",
    menubar:true,
    statusbar: false,
    // height: 250,
    skin: 'oxide-dark',
    content_css: 'dark',
    branding: false
});

const displayTime = function(){
    const date = new Date();
    const time = date.toLocaleTimeString();
    const timeEl = document.getElementById("time");
    timeEl.innerText = time;
}
setInterval(displayTime, 1000);
