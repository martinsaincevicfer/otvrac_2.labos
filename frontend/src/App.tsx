import Datatable from "./components/Datatable.tsx";
import Index from "./components";
import {BrowserRouter, Navigate, Route, Router, Routes} from "react-router-dom";

function App() {
  return (
      <BrowserRouter>
          <Routes>
              <Route path="/" element={<Navigate to="/index" />} />
              <Route path="/datatable" Component={Datatable}/>
              <Route path="/index" Component={Index}/>
          </Routes>
      </BrowserRouter>
  )
}

export default App
