# Quick Start Guide - Relay Node Dashboard

## 🚀 Setup Instructions

### Step 1: Navigate to Frontend Directory
```powershell
cd c:\Users\rithi\Downloads\relay\relay-frontend
```

### Step 2: Install Dependencies
```powershell
npm install axios
npm install
```

### Step 3: Start the Development Server
```powershell
npm start
```

The application will automatically open at **http://localhost:3000**

---

## ✅ Prerequisites Checklist

- ✅ Node.js installed (check: `node --version`)
- ✅ npm installed (check: `npm --version`)
- ✅ Spring Boot backend running on `http://localhost:8080`
- ✅ MongoDB running on `localhost:27017`

---

## 📁 Project Structure

```
relay-frontend/
│
├── public/
│   └── index.html                    # HTML root file
│
├── src/
│   ├── App.js                        # Main React component
│   │   ├── Node Registration
│   │   ├── Health Check
│   │   └── Node Lookup
│   │
│   ├── App.css                       # Component styling
│   │   ├── Grid layout
│   │   ├── Card designs
│   │   ├── Button styles
│   │   └── Responsive design
│   │
│   ├── api.js                        # Axios API service
│   │   ├── registerNode()
│   │   ├── getNode(nodeId)
│   │   └── checkHealth()
│   │
│   ├── index.js                      # React entry point
│   └── index.css                     # Global styles
│
├── package.json                      # Dependencies and scripts
├── .gitignore                        # Git ignore rules
├── README.md                         # Full documentation
└── QUICKSTART.md                     # This file
```

---

## 🎨 Component Breakdown

### App.js Features
- **registerNode()** - Handles POST request to `/api/node/register`
- **checkHealth()** - Handles GET request to `/api/node/health`
- **lookupNode()** - Handles GET request to `/api/node/{nodeId}`
- **State Management** - React hooks for loading, error, and data states
- **UI Rendering** - Displays results with success/error containers

### api.js
- Axios instance with base URL `http://localhost:8080/api`
- Three API methods: `registerNode()`, `getNode()`, `checkHealth()`
- Error handling and response formatting

### App.css
- Modern gradient background (purple to pink)
- Responsive grid with auto-fit columns
- Card hover effects and smooth transitions
- Mobile-friendly breakpoints at 768px
- Color-coded badges and status indicators

---

## 🔄 API Communication Flow

```
User clicks button
    ↓
React Component Handler (handleRegisterNode, etc.)
    ↓
Axios API call via api.js
    ↓
Spring Boot Backend @ localhost:8080
    ↓
MongoDB stores/retrieves data
    ↓
Response returned to frontend
    ↓
React state updated
    ↓
UI re-renders with results
```

---

## 📊 Expected Response Examples

### Health Check Response
```json
{
  "service": "Relay Node Service",
  "status": "healthy"
}
```

### Node Registration Response
```json
{
  "success": true,
  "message": "Node registered successfully",
  "data": {
    "nodeId": "a1b2c3d4e5f6...",
    "publicKey": "MIIBIjANBgkq...",
    "trustScore": 100,
    "malicious": false,
    "databaseId": "507f1f77bcf86cd799439011"
  }
}
```

### Node Lookup Response
```json
{
  "success": true,
  "data": {
    "nodeId": "a1b2c3d4e5f6...",
    "publicKey": "MIIBIjANBgkq...",
    "trustScore": 100,
    "malicious": false,
    "databaseId": "507f1f77bcf86cd799439011"
  }
}
```

---

## 🛠️ Useful Commands

```powershell
# Install dependencies only
npm install

# Start development server
npm start

# Build for production
npm run build

# Run tests
npm test

# Eject configuration (⚠️ irreversible)
npm eject
```

---

## 🐛 Troubleshooting

### Issue: "Cannot find module 'axios'"
**Solution:**
```powershell
npm install axios
npm install
```

### Issue: "CORS error" or "Failed to fetch"
**Solution:** Backend not running or wrong port
```powershell
cd c:\Users\rithi\Downloads\relay\demo
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

### Issue: Port 3000 already in use
**Solution:** React will ask to use a different port - press Y

### Issue: Blank page or error 404
**Solution:** Check browser console (F12 → Console) for errors

---

## ✨ Features Overview

### 1. Server Health Check
- Status: Online/Offline
- Service name verification
- One-click health monitoring

### 2. Node Registration
- Automatic RSA key generation
- SHA-256 node ID creation
- Default trust score: 100
- Malicious flag: false by default
- MongoDB persistence

### 3. Node Lookup
- Search by node ID
- Full node details retrieval
- Enter key support for quick search
- No results error handling

---

## 🔐 Security Features

- ✅ HTTPS-ready API configuration
- ✅ CORS headers validation
- ✅ Error message sanitization
- ✅ Secure node ID hashing (SHA-256)
- ✅ RSA key encryption

---

## 📱 Responsive Design Breakpoints

- **Desktop (1200px+)**: 3-column grid
- **Tablet (768px-1199px)**: 2-column grid
- **Mobile (<768px)**: 1-column layout

---

## 🎓 Learning Resources

- [React Documentation](https://react.dev)
- [Axios Documentation](https://axios-http.com)
- [CSS Grid Guide](https://css-tricks.com/snippets/css/complete-guide-grid/)
- [Spring Boot Docs](https://spring.io/projects/spring-boot)

---

## 📝 Next Steps

1. ✅ Install dependencies: `npm install`
2. ✅ Start frontend: `npm start`
3. ✅ Test health endpoint
4. ✅ Register a new node
5. ✅ Lookup node by ID
6. ✅ Build for production: `npm run build`

---

**Happy coding! 🚀**
