import { Outlet } from 'react-router-dom'
import TopNavbar from '../components/TopNavbar'

export default function Root() {
  return (
    <>
        <TopNavbar />
        <Outlet />
    </>
  )
}
