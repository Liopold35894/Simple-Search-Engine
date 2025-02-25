package com.simplesearch;

import java.io.*;
import java.net.URLEncoder;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;

/*
 * SimpleSearchEngine
 * A simple search engine that allows users to search for websites based on keywords.
 * It connects to an Oracle database and retrieves results based on the search term.
 * This is a servlet that handles the search functionality.
 * It uses JDBC to connect to the database and perform SQL queries.
 * The results are displayed in a paginated format.
 */

public class SearchServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchQuery = request.getParameter("searchTerm");
        int page = 1;
        int resultsPerPage = 30;

        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1;
        }

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().println("Error: No suitable driver found for jdbc:oracle:thin:@localhost:1521:FREE");
            return;
        }

        // Set response content type
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Start HTML output
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("    <title>Search Results - Simple Search Engine</title>");
        out.println("    <style>");
        out.println("        * {");
        out.println("            margin: 0;");
        out.println("            padding: 0;");
        out.println("            box-sizing: border-box;");
        out.println("        }");
        out.println("        body {");
        out.println("            font-family: Arial, sans-serif;");
        out.println("            background-color: rgb(10, 10, 40);");
        out.println("            color: #d1fcfe;"); // Light text from search input border
        out.println("            line-height: 1.5;");
        out.println("        }");
        out.println("        .header {");
        out.println("            background-color: rgb(10, 10, 40);"); // Same as body
        out.println("            padding: 1rem 0;");
        out.println("            border-bottom: 1px solid #23467d;"); // Button color
        out.println("            margin-bottom: 2rem;");
        out.println("        }");
        out.println("        .search-container {");
        out.println("            max-width: 600px;");
        out.println("            margin: 0 auto;");
        out.println("            display: flex;");
        out.println("            gap: 1rem;");
        out.println("            align-items: center;");
        out.println("        }");
        out.println("        .logo {");
        out.println("            font-size: 1.5rem;");
        out.println("            font-weight: bold;");
        out.println("            color: #256d7f;"); // Welcome page logo color
        out.println("            text-decoration: none;");
        out.println("        }");
        out.println("        .search-bar {");
        out.println("            flex-grow: 1;");
        out.println("            padding: 0.5rem 1rem;");
        out.println("            border: 1px solid #d1fcfe;"); // Search input border
        out.println("            border-radius: 24px;");
        out.println("            background: rgba(255, 255, 255, 0.1);"); // Transparent white
        out.println("            display: flex;");
        out.println("            align-items: center;");
        out.println("        }");
        out.println("        .search-input {");
        out.println("            width: 100%;");
        out.println("            border: none;");
        out.println("            outline: none;");
        out.println("            font-size: 1rem;");
        out.println("            padding: 0.25rem;");
        out.println("            background: transparent;");
        out.println("            color: #d1fcfe;"); // Input text color
        out.println("        }");
        out.println("        .results-container {");
        out.println("            max-width: 700px;");
        out.println("            margin: 0 auto;");
        out.println("            padding: 0 1rem;");
        out.println("            padding-bottom: 100px;");
        out.println("        }");
        out.println("        .result {");
        out.println("            margin-bottom: 1.5rem;");
        out.println("            padding: 1rem;");
        out.println("            background: rgba(255, 255, 255, 0.05);"); // Semi-transparent cards
        out.println("            border-radius: 8px;");
        out.println("            border: 1px solid #23467d;"); // Button color
        out.println("        }");
        out.println("        .result-title {");
        out.println("            font-size: 1.25rem;");
        out.println("            color: #256d7f;"); // Logo color for titles
        out.println("            text-decoration: none;");
        out.println("            display: block;");
        out.println("            margin-bottom: 0.25rem;");
        out.println("        }");
        out.println("        .result-title:hover {");
        out.println("            color: #1a9cad;"); // Brighter teal for hover
        out.println("            text-decoration: underline;");
        out.println("        }");
        out.println("        .result-url {");
        out.println("            color: #1a9cad;"); // Bright teal for URLs
        out.println("            font-size: 0.875rem;");
        out.println("            margin-bottom: 0.25rem;");
        out.println("        }");
        out.println("        .result-description {");
        out.println("            color: #d1fcfe;"); // Light text from search border
        out.println("            font-size: 0.9375rem;");
        out.println("            opacity: 0.9;");
        out.println("        }");
        out.println("        .results-count {");
        out.println("            color: #256d7f;"); // Logo color
        out.println("            font-size: 0.875rem;");
        out.println("            margin-bottom: 1.5rem;");
        out.println("        }");
        out.println("        .search-meta {");
        out.println("            color: #256d7f;");
        out.println("            margin-bottom: 1.5rem;");
        out.println("            font-size: 0.9rem;");
        out.println("        }");
        out.println("        .pagination {");
        out.println("            margin: 2rem 0;");
        out.println("            display: flex;");
        out.println("            gap: 1rem;");
        out.println("            justify-content: center;");
        out.println("            flex-wrap: wrap;");
        out.println("        }");
        out.println("        .page-number {");
        out.println("            padding: 0.5rem 1rem;");
        out.println("            border-radius: 20px;");
        out.println("            background: rgba(255,255,255,0.1);");
        out.println("            color: #d1fcfe;");
        out.println("            text-decoration: none;");
        out.println("            transition: background 0.3s ease;");
        out.println("        }");
        out.println("        .page-number:hover {");
        out.println("            background: rgba(255, 255, 255, 0.2);");
        out.println("            }");
        out.println("        .current-page {");
        out.println("            padding: 0.5rem 1rem;");
        out.println("            border-radius: 20px;");
        out.println("            background: #23467d");
        out.println("            color: white;");
        out.println("        }");
        out.println("        .search-footer {");
        out.println("            background-color: rgb(10, 10, 40);");
        out.println("            padding: 1.5rem 0;");
        out.println("            margin-top: 3rem;");
        out.println("            border-top: 1px solid #23467d;");
        out.println("            position: relative;");
        out.println("        }");
        out.println("        .footer-content {");
        out.println("            max-width: 700px;");
        out.println("            margin: 0 auto;");
        out.println("            display: flex;");
        out.println("            justify-content: center;");
        out.println("            gap: 2rem;");
        out.println("            flex-wrap: wrap;");
        out.println("        }");
        out.println("        .footer-content a {");
        out.println("            color: #d1fcfe;");
        out.println("            text-decoration: none;");
        out.println("            font-size: 0.9rem;");
        out.println("            transition: color 0.3s ease;");
        out.println("        }");
        out.println("        .footer-content a:hover {");
        out.println("            color: #256d7f;");
        out.println("            text-decoration: underline;");
        out.println("        }");
        out.println("        .footer-text {");
        out.println("            color: rgba(209, 252, 254, 0.7);");
        out.println("            font-size: 0.9rem;");
        out.println("        }");
        out.println("        .no-results {");
        out.println("           display: flex;");
        out.println("           flex-direction: column;");
        out.println("           justify-content: center;"); 
        out.println("           align-items: center;");  
        out.println("           text-align: center;");
        out.println("        }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");

        // Header with search bar (matches welcome page style)
        out.println("<div class='header'>");
        out.println("    <div class='search-container'>");
        out.println("        <a href='https://simple-search-engine.onrender.com/SimpleSearchEngine/' class='logo'>Simple Search</a>");
        out.println("        <form action='search' method='get' class='search-bar'>");
        out.println("            <input type='text' name='searchTerm' class='search-input' value='" + (searchQuery != null ? searchQuery : "") + "'/>");
        out.println("        </form>");
        out.println("    </div>");
        out.println("</div>");
        
        out.println("<div class='results-container'>");

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            try {
                // Database connection
                String db_url = System.getenv("DB_URL");
                String db_username = System.getenv("DB_USER");
                String db_password = System.getenv("DB_PASS");

                Connection conn = DriverManager.getConnection(db_url, db_username, db_password);

                // Get total results count
                String countSql = "SELECT COUNT(*) AS total FROM websites WHERE LOWER(title) LIKE LOWER(?) OR LOWER(description) LIKE LOWER(?) OR LOWER(keywords) LIKE LOWER(?) OR LOWER(url) LIKE LOWER(?)";
                PreparedStatement countStmt = conn.prepareStatement(countSql);
                String searchTerm = ("%" + searchQuery + "%").toLowerCase();
                countStmt.setString(1, searchTerm);
                countStmt.setString(2, searchTerm);
                countStmt.setString(3, searchTerm);
                countStmt.setString(4, searchTerm);
                
                ResultSet countRs = countStmt.executeQuery();
                int totalResults = 0;
                if(countRs.next()) {
                    totalResults = countRs.getInt("total");
                }
                int totalPages = (int) Math.ceil((double) totalResults / resultsPerPage);

                // Create a SQL query with a LIKE condition for search, with pagination
                String sql = "SELECT * FROM ("
                       + "    SELECT w.*, ROWNUM rnum FROM ("
                       + "        SELECT title, description, url FROM websites"
                       + "        WHERE LOWER(title) LIKE LOWER(?)"
                       + "           OR LOWER(description) LIKE LOWER(?)"
                       + "           OR LOWER(keywords) LIKE LOWER(?)"
                       + "           OR LOWER(url) LIKE LOWER(?)"
                       + "        ORDER BY title"
                       + "    ) w WHERE ROWNUM <= ?"
                       + ") WHERE rnum > ?";

                PreparedStatement stmt = conn.prepareStatement(sql);

                stmt.setString(1, searchTerm);
                stmt.setString(2, searchTerm);
                stmt.setString(3, searchTerm);
                stmt.setString(4, searchTerm);
                stmt.setInt(5, page * resultsPerPage);  // upper bound
                stmt.setInt(6, (page - 1) * resultsPerPage); // lower bound

                // Execute the query
                ResultSet rs = stmt.executeQuery();

                // Display the results
                boolean resultsFound = false;
                int resultCount = 0;

                while (rs.next()) {
                    resultsFound = true;
                    resultCount++;

                    String title = rs.getString("title");
                    String description = rs.getString("description");
                    String url = rs.getString("url");

                    out.println("<div class='result'>");
                    out.println("   <div class='result-url'>" + url + "</div>");
                    out.println("   <a href='" + url + "' class='result-title' target='_blank'>" + title + "</a>");
                    out.println("   <p class='result-description'>" + description + "</p>");
                    out.println("</div>");
                }

                if (resultsFound) {
                    out.println("<div class='results-count'>About " + resultCount + " results</div>");
                } else {
                    out.println("<div class='result no-results'>");
                    out.println("    <p class='result-description'>No results found for <strong>" + searchQuery + "</strong>.</p>");
                    out.println();
                    out.println("    <p class='result-description'>Suggestions:</p>");
                    out.println("    <ul class='result-description'>");
                    out.println("        <li>Make sure all words are spelled correctly</li>");
                    out.println("        <li>Try different keywords</li>");
                    out.println("        <li>e.g. liopold, portfolio, github</li>");
                    out.println("    </ul>");
                    out.println("</div>");
                }

                // Pagination controls
                out.println("<div class='pagination'>");
                if(page > 1) {
                    out.println("<a href='search?searchTerm=" + URLEncoder.encode(searchQuery, "UTF-8") 
                            + "&page=" + (page-1) + "' class='page-number'>Previous</a>");
                }
                
                for(int i = 1; i <= totalPages; i++) {
                    if(i == page) {
                        out.println("<span class='current-page'>" + i + "</span>");
                    } else {
                        out.println("<a href='search?searchTerm=" + URLEncoder.encode(searchQuery, "UTF-8") 
                                + "&page=" + i + "' class='page-number'>" + i + "</a>");
                    }
                }
                
                if(page < totalPages) {
                    out.println("<a href='search?searchTerm=" + URLEncoder.encode(searchQuery, "UTF-8") 
                            + "&page=" + (page+1) + "' class='page-number'>Next</a>");
                }
                out.println("</div>");

                // Close the resources/connections
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                // Handle errors for JDBC
                e.printStackTrace();
                out.println("<p>Error: " + e.getMessage() + "</p>");
            }
        } else {
            out.println("<div class='result'>");
            out.println("    <p class='result-description'>Please enter a search term in the box above.</p>");
            out.println("</div>");
        }

        out.println("</div>"); // Close results-container

        // Footer
        out.println("<footer class='search-footer'>");
        out.println("    <div class='footer-content'>");
        out.println("        <a href='#'>About</a>");
        out.println("        <span class='footer-text'>|</span>");
        out.println("        <a href='#'>Privacy</a>");
        out.println("        <span class='footer-text'>|</span>");
        out.println("        <a href='#'>Terms</a>");
        out.println("        <span class='footer-text'>|</span>");
        out.println("        <span class='footer-text'>Simple Search Engine v1.0</span>");
        out.println("    </div>");
        out.println("</footer>");

        // End HTML output
        out.println("</body></html>");
    }
}