document.addEventListener("DOMContentLoaded", function () {
    const users = [
        { id: 1, name: "User 1", messages: ["Hey, let's join a guitar session!"] },
        { id: 2, name: "User 2", messages: ["You want to learn piano?"] },
    ];

    let currentUser = users[0];

    // Get the username from localStorage
    const userProfile = JSON.parse(localStorage.getItem("userProfile"));
    const username = userProfile?.username || "Me";

    // Retrieve stored messages from localStorage
    const storedMessages = JSON.parse(localStorage.getItem("messages")) || [];

    function renderMessages() {
        const chatArea = document.getElementById("chat-area");
        chatArea.innerHTML = "";

        const currentMessages = storedMessages.filter(
            msg => msg.to === currentUser.name || msg.from === currentUser.name
        );

        currentMessages.forEach((messageObj, index) => {
            const messageContainer = document.createElement("div");
            messageContainer.className = "message-container";

            const userIcon = document.createElement("div");
            userIcon.className = "user-icon";
            messageContainer.appendChild(userIcon);

            const messageDiv = document.createElement("div");
            messageDiv.className = "message";

            const infoLine = document.createElement("div");
            infoLine.className = "info-line";
            infoLine.style.marginBottom = "0.5em";

            const userNameSpan = document.createElement("span");
            userNameSpan.className = "text";
            userNameSpan.innerHTML = `<strong>${messageObj.from}</strong>`;
            infoLine.appendChild(userNameSpan);

            const timeSpan = document.createElement("span");
            timeSpan.className = "text text--sm";
            timeSpan.style.color = "#4f4f4f";
            timeSpan.style.marginLeft = "5px";
            const time = new Date(messageObj.time);
            timeSpan.innerText = `Today at ${time.toLocaleTimeString()}`;
            infoLine.appendChild(timeSpan);

            messageDiv.appendChild(infoLine);

            const messageText = document.createElement("div");
            messageText.className = "text text--md";
            messageText.style.lineHeight = "1.5em";
            messageText.innerText = messageObj.text;
            messageDiv.appendChild(messageText);

            messageContainer.appendChild(messageDiv);
            chatArea.appendChild(messageContainer);
        });
    }

    function updateToolbar() {
        const toolbarContainer = document.querySelector("#toolbar .container");
        toolbarContainer.innerHTML = `
            <i class="fas fa-at" style="color: #666;"></i>
            <span class="text" style="margin-left: 10px;"><strong>${currentUser.name}</strong></span>
            <div class="status-circle status-circle--online"></div>
            <div class="vertical-rule"></div>
            <div class="aka"></div>
        `;
    }

    function addUserClickHandlers() {
        const userContainers = document.querySelectorAll(".user-container");
        userContainers.forEach((container, index) => {
            container.addEventListener("click", () => {
                document.querySelector(".user-container--active")?.classList.remove("user-container--active");
                container.classList.add("user-container--active");
                currentUser = users[index];
                renderMessages();
                updateToolbar();
            });
        });
    }

    function addMessage() {
        const chatInput = document.getElementById("chat-input");
        const input = chatInput.querySelector("input");

        if (input.value.trim()) {
            const newMessage = {
                from: username,
                to: currentUser.name,
                time: new Date().toISOString(),
                text: input.value.trim()
            };
            storedMessages.push(newMessage);
            localStorage.setItem("messages", JSON.stringify(storedMessages));
            input.value = "";
            renderMessages();
        }
    }

    function initChatInput() {
        const chatInput = document.getElementById("chat-input");
        chatInput.innerHTML = `
            <input type="text" placeholder="Type a message..." style="flex: 1; padding: 10px; margin-right: 10px;">
            <button id="send-button" style="padding: 10px; background-color: #22DDAA; color: white; border: none; border-radius: 5px; cursor: pointer;">Send</button>
        `;

        const sendButton = document.getElementById("send-button");
        sendButton.addEventListener("click", addMessage);

        const inputField = chatInput.querySelector("input");
        inputField.addEventListener("keydown", function (event) {
            if (event.key === "Enter") {
                addMessage();
            }
        });
    }

    renderMessages();
    updateToolbar();
    addUserClickHandlers();
    initChatInput();
});
