import { BrowserRouter, Switch, Route } from "react-router-dom";
import Home from "./Home";
import Login from "./login";
import Navbar from "./Navbar";
import Orders from "./Orders";
import ProductManagement from "./ProductManagement";

function Routes() {
    return (
        <BrowserRouter>
            <Navbar />
            <Switch>
                <Route path="/product-management">
                    <ProductManagement />
                </Route>
                <Route path="/login">
                    <Login />
                </Route>
                <Route path="/orders">
                    <Orders />
                </Route>
                <Route path="/">
                    <Home />
                </Route>
            </Switch>
        </BrowserRouter>
    )
}

export default Routes;