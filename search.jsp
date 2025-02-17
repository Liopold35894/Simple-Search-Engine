<!--
 This is welcome page with HTML, CSS, and JavaScript
 This code creates a simple search engine interface with a starry background,
 falling stars, and a search history feature.
 The search history is stored in localStorage and can be displayed or cleared.
 The search input allows users to enter a keyword and submit it for searching.
 The search history is displayed in a dropdown-like interface with options to delete individual entries or clear all history.
 The falling stars and twinkling background are created using CSS animations.
 The search history is dynamically updated and displayed using JavaScript.
 The code is designed to be responsive and visually appealing.
 -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="0">
    <title>Simple Search Engine</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: rgb(10, 10, 40);
            margin: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            overflow: hidden;
            position: relative;
        }

        /* Starry background */
        .stars {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            pointer-events: none;
        }

        /* Falling star animation */
        .falling-star {
            position: absolute;
            width: 4px;
            height: 4px;
            background: white;
            border-radius: 50%;
            animation: fall 7s linear infinite;
            visibility: hidden;
            filter: blur(0px);
        }

        .falling-star::before {
            content: '';
            position: absolute;
            width: 100px;
            height: 2px;
            background: linear-gradient(
                35deg, 
                rgba(255, 255, 255, 1) 0%, 
                rgba(255, 255, 255, 0.5) 50%, 
                rgba(255, 255, 255, 0) 100%
            );
            transform-origin: 100% 50%;
            top: 0;
        }

        @keyframes fall {
            0% {
                transform: translate(100vw, -100vh) rotate(-45deg) scale(1);
                visibility: visible;
            }
            100% {
                transform: translate(-100vw, 100vh) rotate(-45deg) scale(1);
            }
        }

        /* Twinkling stars */
        .twinkling {
            position: absolute;
            background: white;
            border-radius: 50%;
            animation: twinkle var(--duration) ease-in-out infinite;
        }

        @keyframes twinkle {
            0%, 100% { 
                opacity: 0.3; 
                transform: scale(1);
            }
            50% { 
                opacity: 1;
                transform: scale(1.2);
            }
        }

        /* Existing search box styles */
        .search-box {
            text-align: center;
            margin: auto;
            position: relative;
            z-index: 1;
        }

        .logo {
            font-size: 3rem;
            font-weight: bold;
            color: #256d7f;
            margin-bottom: 20px;
            text-shadow: 0 0 10px rgba(37, 109, 127, 0.5);
            z-index: 1;
        }

        .search-input {
            width: 400px;
            padding: 10px;
            font-size: 1.2rem;
            border: 1px solid #d1fcfe;
            border-radius: 24px;
            outline: none;
            box-shadow: 0px 4px 6px rgba(255, 255, 255, 0.1);
            background: rgba(255, 255, 255, 0.1);
            color: #d1fcfe;
            z-index: 1;
        }

        .search-button {
            margin-left: 10px;
            padding: 10px 20px;
            font-size: 1rem;
            background-color: #23467d;
            color: white;
            border: none;
            border-radius: 24px;
            cursor: pointer;
            box-shadow: 0px 4px 6px rgba(255, 255, 255, 0.1);
            transition: background-color 0.3s;
            z-index: 1;
        }

        .search-button:hover {
            background-color: #102343;
        }

        /* Search History Styles */
        .search-history {
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            z-index: 1000;
            width: 400px;
            background: rgba(10, 10, 40, 0.95);
            border: 1px solid #23467d;
            border-radius: 12px;
            padding: 1rem;
            box-shadow: 0 4px 12px rgba(0,0,0,0.2);
            backdrop-filter: blur(5px);
            display: none;
        }

        .history-title {
            color: #256d7f;
            margin-bottom: 1rem;
            font-size: 1.1rem;
        }

        .history-list {
            list-style: none;
            margin: 0;
            padding: 0;
        }

        .history-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 0.5rem;
            margin: 0.25rem 0;
            border-radius: 6px;
            transition: background 0.3s ease;
        }

        .history-item:hover {
            background: rgba(255,255,255,0.05);
        }

        .history-link {
            color: #d1fcfe;
            text-decoration: none;
            flex-grow: 1;
            cursor: pointer;
            padding: 0.25rem;
        }

        .history-link:hover {
            color: #1a9cad;
            text-decoration: underline;
        }

        .history-timestamp {
            color: rgba(209, 252, 254, 0.5);
            font-size: 0.8rem;
            margin-left: 1rem;
        }

        .delete-btn {
            background: none;
            border: none;
            color: #ff4d4d;
            cursor: pointer;
            padding: 0 0.5rem;
            font-size: 1.2rem;
            line-height: 1;
        }

        .delete-btn:hover {
            color: #ff1a1a;
        }

        .clear-btn {
            width: 100%;
            margin-top: 1rem;
            padding: 0.5rem;
            background: rgba(255,77,77,0.1);
            border: 1px solid #ff4d4d;
            color: #ff4d4d;
            border-radius: 6px;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .clear-btn:hover {
            background: rgba(255,77,77,0.2);
        }

        .close-btn {
            position: absolute;
            top: 0.5rem;
            right: 0.5rem;
            background: none;
            border: none;
            color: #d1fcfe;
            font-size: 1.5rem;
            cursor: pointer;
            padding: 0 0.5rem;
        }

        .close-btn:hover {
            background: rgba(255,77,77,0.2);
        }

        .toggle-history-btn {
            margin-top: 1rem;
            padding: 0.5rem 1rem;
            font-size: 0.9rem;
            background-color: #23467d;
            color: white;
            border: none;
            border-radius: 20px;
            cursor: pointer;
            transition: background-color 0.3s;
            z-index: 1;
        }
        
        .toggle-history-btn:hover {
            background-color: #102343;
        }

        .no-history {
            color: rgba(209, 252, 254, 0.5);
            text-align: center;
            margin: 1rem 0;
        }
    </style>
</head>
<body>
    <div class="stars" id="stars-container"></div>
    
    <div class="search-box">
        <div class="logo">Simple Search Engine</div>
        <form action="search" method="get">
            <input type="text" name="searchTerm" class="search-input" placeholder="Enter keyword to search" />
            <button type="submit" class="search-button">Search</button>
        </form>
        <!-- New toggle button for search history -->
        <button id="toggleHistoryBtn" class="toggle-history-btn">Show History</button>
    </div>

    <script>
        // Create falling stars and twinkling background
        function createStars() {
            const container = document.getElementById('stars-container');
            container.innerHTML = '';
            
            // Create falling stars
            for(let i = 0; i < 20; i++) {
                const star = document.createElement('div');
                star.className = 'falling-star';
                star.style.left = Math.random() * 100 + 'vw';
                star.style.animationDuration = (Math.random() * 3 + 5) + 's';
                star.style.animationDelay = Math.random() * 5 + 's';
                // Random sparkle timing
                star.style.setProperty('--sparkle-delay', Math.random() * 2 + 's');
                container.appendChild(star);
            }

            // Create twinkling stars
            for(let i = 0; i < 50; i++) {
                const star = document.createElement('div');
                star.className = 'twinkling';
                star.style.width = Math.random() * 3 + 'px';
                star.style.height = star.style.width;
                star.style.left = Math.random() * 100 + 'vw';
                star.style.top = Math.random() * 100 + 'vh';
                star.style.setProperty('--duration', Math.random() * 3 + 2 + 's');

                // Random sparkle effect
                if(Math.random() > 0.7) {
                    star.style.animation += ', sparkle 0.5s ease-in-out infinite';
                }

                container.appendChild(star);
            }
        }

        function initializeSearch() {
            createStars();
            setupSearchHistory();
        }

        document.addEventListener('DOMContentLoaded', initializeSearch);

        // Save search term to localStorage
        function saveSearchTerm(term) {
            let history = JSON.parse(localStorage.getItem('searchHistory')) || [];
            history.unshift({
                id: Date.now(), // Unique identifier
                term: term,
                timestamp: new Date().toISOString()
            });
            history = history.slice(0, 10);
            localStorage.setItem('searchHistory', JSON.stringify(history));
        }

        function setupSearchHistory() {
            const form = document.querySelector('form');
            const input = document.querySelector('.search-input');
            const searchBox = document.querySelector('.search-box');
            const historyContainer = document.createElement('div');
            
            // Style class names
            historyContainer.className = 'search-history';
            searchBox.parentNode.insertBefore(historyContainer, searchBox.nextSibling);

            const closeBtn = document.createElement('button');
            closeBtn.innerHTML = '&times;';
            closeBtn.textContent = 'Close';
            closeBtn.className = 'close-btn';
            closeBtn.addEventListener('click', () => {
                historyContainer.style.display = 'none';
            });
            historyContainer.appendChild(closeBtn);

            const dynamicContent = document.createElement('div');
            dynamicContent.className = 'dynamic-content';
            historyContainer.appendChild(dynamicContent);

            // Load and display search history
            function loadSearchHistory() {
                const history = JSON.parse(localStorage.getItem('searchHistory')) || [];

                // Clear existing dynamic content
                dynamicContent.innerHTML = '';
                dynamicContent.innerHTML = '<h3 class="history-title">Search History</h3>';
                
                if(history.length === 0) {
                    dynamicContent.innerHTML += '<p class="no-history">No search history yet</p>';
                    return;
                }
                
                const list = document.createElement('ul');
                list.className = 'history-list';
                    
                history.forEach(entry => {
                    const listItem = document.createElement('li');
                    listItem.className = 'history-item';
                    listItem.dataset.id = entry.id;
                        
                    // Create clickable history items
                    const link = document.createElement('a');
                    link.href = 'javascript:void(0);';
                    link.textContent = entry.term;
                    link.className = 'history-link';
                        
                    // Add click handler to reuse search
                    link.addEventListener('click', function() {
                        input.value = entry.term;
                        form.submit();
                    });

                    // Add delete button
                    const deleteBtn = document.createElement('button');
                    deleteBtn.innerHTML = '&times;';
                    deleteBtn.className = 'delete-btn';
                    deleteBtn.addEventListener('click', function(e) {
                        e.stopPropagation();
                        removeFromHistory(entry.id);
                    });

                    // Add timestamp
                    const timestamp = document.createElement('span');
                    timestamp.className = 'history-timestamp';
                    timestamp.textContent = new Date(entry.timestamp).toLocaleTimeString();

                    listItem.appendChild(link);
                    listItem.appendChild(timestamp);
                    listItem.appendChild(deleteBtn);
                    list.appendChild(listItem);
                });

                dynamicContent.appendChild(list);

                // Add clear all button
                const clearAll = document.createElement('button');
                clearAll.textContent = 'Clear All History';
                clearAll.className = 'clear-btn';
                clearAll.addEventListener('click', clearHistory);
                dynamicContent.appendChild(clearAll);
            }

            function removeFromHistory(id) {
                let history = JSON.parse(localStorage.getItem('searchHistory')) || [];
                history = history.filter(entry => entry.id !== id);
                localStorage.setItem('searchHistory', JSON.stringify(history));
                loadSearchHistory();
            }

            function clearHistory() {
                localStorage.removeItem('searchHistory');
                loadSearchHistory();
            }
    
            form.addEventListener('submit', function(event) {
                const searchTerm = input.value.trim();
                if (searchTerm) {
                    saveSearchTerm(searchTerm);
                }
            });

            document.getElementById('toggleHistoryBtn').addEventListener('click', function() {
                historyContainer.style.display = 'block';
                loadSearchHistory();
            });

            // Close history when clicking outside of it
            document.addEventListener('click', function(event) {
                if (!historyContainer.contains(event.target) && event.target.id !== 'toggleHistoryBtn') {
                    historyContainer.style.display = 'none';
                }
            });
    
            loadSearchHistory();
        }
    </script>
</body>
</html>