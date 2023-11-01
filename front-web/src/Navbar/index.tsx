import './styles.css';
import { ReactComponent as Logo } from './logo.svg';
import { Link } from 'react-router-dom';

function Navbar (){
    return(
        <nav className="main-navbar">
            <div>
                <Logo />
                <Link to="/" className="logo-text">DS Delivery</Link>
            </div>
            <Link to='/login' className='login-text'>Login</Link>
        </nav>
    )
}

export default Navbar;