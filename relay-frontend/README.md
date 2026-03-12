# Relay Node Dashboard

A React frontend for the Blockchain-Based Reputation-Aware Relay Selection System. This dashboard provides an intuitive interface to interact with the Spring Boot backend for managing P2P network nodes.

## Features

✅ **Node Registration** - Register new nodes in the P2P relay network  
✅ **Health Check** - Monitor backend server status  
✅ **Node Lookup** - Retrieve information about registered nodes  
✅ **Real-time API Integration** - Axios-based REST API calls  
✅ **Clean UI/UX** - Modern, responsive dashboard design  
✅ **Error Handling** - Comprehensive error messages and user feedback  

## Prerequisites

- Node.js 14+ ([Download](https://nodejs.org/))
- npm 6+ (comes with Node.js)
- Spring Boot backend running on `http://localhost:8080`

## Project Structure

```
relay-frontend/
├── public/
│   └── index.html              # Main HTML file
├── src/
│   ├── App.js                  # Main React component
│   ├── App.css                 # Component styling
│   ├── api.js                  # API service with Axios
│   ├── index.js                # React entry point
│   └── index.css               # Global styles
├── package.json                # Project dependencies
├── .gitignore                  # Git ignore rules
└── README.md                   # This file
```

## Installation

1. Navigate to the project directory:
```bash
cd relay-frontend
```

2. Install dependencies:
```bash
npm install
```

This will install React, ReactDOM, Axios, and react-scripts.

## Running the Application

Start the development server:
```bash
npm start
```

The application will open at `http://localhost:3000` in your default browser.

**Important:** Make sure the Spring Boot backend is running on `http://localhost:8080` before starting the frontend.

## API Service (api.js)

The `api.js` file provides a centralized API service with three main methods:

```javascript
// Register a new node
NodeAPI.registerNode()

// Get node information
NodeAPI.getNode(nodeId)

// Check server health
NodeAPI.checkHealth()
```

## Component Features

### 1. Health Check
- Button to check server status
- Displays service name and status
- Shows error if backend is unavailable

### 2. Node Registration
- Automatically generates RSA keys and node identity
- Displays registered node details:
  - Node ID (SHA-256 hash)
  - Public Key
  - Trust Score (default: 100)
  - Malicious flag (default: false)
  - Database ID (MongoDB ID)

### 3. Node Lookup
- Input field to enter a node ID
- Retrieves node details from backend
- Displays full node information
- Supports Enter key for quick search

## API Endpoints

The frontend communicates with the following backend endpoints:

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/node/health` | Check server health |
| POST | `/api/node/register` | Register a new node |
| GET | `/api/node/{nodeId}` | Get node information |

## Styling

The application features:
- **Gradient Background**: Purple-to-pink gradient
- **Responsive Grid Layout**: Auto-adapting card layout
- **Card Design**: Hover effects and smooth transitions
- **Color-coded Badges**: Status indicators for trust scores and malicious flags
- **Mobile-friendly**: Works on small screens and tablets

## Environment Variables

To customize the backend API URL, create a `.env` file in the project root:

```
REACT_APP_API_URL=http://localhost:8080
```

Then modify `api.js` to use:
```javascript
const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';
```

## Building for Production

Create an optimized production build:
```bash
npm run build
```

The build folder will contain the optimized production-ready files.

## Troubleshooting

### CORS Error
If you see a CORS error, ensure your Spring Boot backend has CORS enabled. Add to your `NodeController`:

```java
@CrossOrigin(origins = "http://localhost:3000")
```

### Backend Connection Issues
- Verify the backend is running: `http://localhost:8080/api/node/health`
- Check the backend URL in `api.js`
- Look at browser console for detailed error messages

### Port Already in Use
If port 3000 is already in use, the app will ask to use a different port:
```
Something is already running on port 3000. Would you like to run the app on another port instead? (Y/n)
```

## Technologies Used

- **React 18**: UI framework
- **Axios 1.6**: HTTP client
- **CSS3**: Modern styling with gradients and flexbox
- **React Hooks**: State management (useState)

## Development Tips

1. Use React DevTools browser extension for debugging
2. Check browser console (F12) for API errors
3. Network tab shows all API requests to the backend
4. Hot module reloading enabled - changes auto-refresh

## Future Enhancements

- Add node deletion functionality
- Implement pagination for node lists
- Add data export to CSV/JSON
- Real-time node status updates with WebSockets
- User authentication and authorization
- Advanced filtering and search capabilities

## Support

For issues or questions, check:
- Backend logs: `java -jar target/demo-0.0.1-SNAPSHOT.jar`
- Frontend console: Press F12 → Console tab
- Network requests: F12 → Network tab

## License

MIT License - See LICENSE file for details
