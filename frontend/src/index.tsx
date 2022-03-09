import React, {ReactNode, ReactNodeArray} from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import {RestCellarDoor} from "./clients/rest/RestCellarDoor";
import { CellarDoorProvider } from './components/CellarDoorContext';

ReactDOM.render(
    <React.StrictMode>
        <CellarDoorProvider cellarDoor={new RestCellarDoor("http://localhost:8080/")}>
            <App />
        </CellarDoorProvider>
    </React.StrictMode>,
    document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
