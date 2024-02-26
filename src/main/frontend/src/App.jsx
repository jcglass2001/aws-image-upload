import { useState, useEffect } from 'react'
import './App.css'
import UserProfiles from './components/UserProfiles'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
    <UserProfiles/>
    </>
  )
}

export default App
