import React, {ReactNode} from "react";
import {CellarDoor} from "../clients/CellarDoor";

const CellarDoorContext = React.createContext<CellarDoor | null>(null);

/**
 * This context is used to provide the current cellar door to the entire application.
 * It allows us to inject the cellar door into any component that needs it, and also swap it out
 * with a different one *cough* GraphQL *cough*.
 *
 * @constructor
 */
export const CellarDoorProvider = (props: {children: ReactNode, cellarDoor?: CellarDoor}) => {
    return (
        <CellarDoorContext.Provider value={props.cellarDoor !== undefined ? props.cellarDoor : null}>
            {props.children}
            </CellarDoorContext.Provider>
    )
};

export const CellarDoorConsumer = CellarDoorContext.Consumer;
