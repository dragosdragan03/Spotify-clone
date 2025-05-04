# Spotify Clone

A Java-based audio player system that simulates the core functionality of Spotify, featuring user management, song/podcast playback, and administration capabilities.

## Project Overview

This project implements a console-based audio streaming platform similar to Spotify. The system manages users, content (songs and podcasts), and creators (artists and hosts) with data stored in local JSON files. The implementation adheres to object-oriented programming principles and various design patterns to ensure code maintainability, scalability, and modularity.

## Features

### User Management
- User registration and authentication
- Multiple user types (regular users, content creators, administrators)
- User profile management
- Personalized playlists and favorites

### Content Management
- Song and podcast libraries
- Playback functionality
- Playlist creation and management

### Creator Tools
- Artist profile management
- Host profile management
- Content upload and management
- Analytics for content creators

### Administrative Features
- System statistics and reporting
- User management capabilities
- Content moderation tools
- Performance monitoring

## Technical Implementation

### Architecture
The project follows a layered architecture with clear separation of concerns:
- Data layer: JSON-based persistence
- Controller layer: User interaction handling

### Design Patterns Implemented
- **Singleton Pattern**: For system configuration and resource management
- **Factory Pattern**: For creating different types of users and content
- **Observer Pattern**: For notification systems
- **Command Pattern**: For handling user commands

### Data Storage
All data is stored locally in JSON format, providing:
- Easy data inspection and modification
- Simplified debugging process
- No database setup required

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 11 or higher
- JSON processing library (e.g., Jackson, Gson)
- IDE (recommended: IntelliJ IDEA or Eclipse)

## Project Structure
```
src/
└── app/           
    ├── admin/     # Admin user
    │   └── AdminManager.java  # Handles administrative operations and statistics
    │
    ├── audio/     # Audio content management
    │   ├── Files/  # Audio file representations
    │   │   ├── Song.java      # Song class with attributes
    │   │   └── Episode.java   # Podcast class with episodes
    │   │
    │   └── Collections/       # Groupings of audio content
    |       ├── Podcast.java   # Collection of episodes
    │       ├── Album.java     # Collection of songs by an artist
    │       └── Playlist.java  # User-created collection of songs
    │
    ├── main/      # Application entry points
    │   └── Main.java          # Program entry point and initialization
    │
    ├── pagination/ # Pagination system implementation
    │   ├── Page.java          # Represents a single page of content
    │   └── PaginationManager.java  # Handles page navigation and content loading
    │
    └── user/      # User management
        ├── entities/          # Different user types
        │   ├── User.java      # Base user class
        │   ├── Artist.java    # Artist-specific functionality
        │   └── Host.java      # Podcast host functionality
        │
        └── management/        # User operations
            └── UserManager.java  # Handles user creation, login, and management
```

## Key Features in Detail

### User Management
- Profile customization options
- Following other users and creators
- Activity history tracking

### Content Discovery
- Search functionality with filters
- Genre and mood-based browsing
- New release notifications

### Playback Features
- Play, pause, stop functionality
- Queue management
- Repeat and shuffle options
- Volume control

### Social Features
- Song likes system for users to express preferences
- Like-based analytics to track popularity metrics
- Top 5 rankings for most-liked songs across the platform
- Top 5 artists based on cumulative song likes
- Top 5 podcasts highlighting most popular audio content
- User profiles with integrated collection of liked content
- Artist insights showing statistics on their most popular works

### Running the Application
Execute the main class to start the application:
```
java -cp <classpath> com.spotifyclone.Main
```
