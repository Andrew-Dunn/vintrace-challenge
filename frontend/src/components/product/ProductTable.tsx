import {CellarDoor} from "../../clients/CellarDoor";
import { CellarDoorConsumer } from "../CellarDoorContext";
import {WineInfo} from "../../clients/types/WineInfo";
import React from "react";
import {ProductDetails} from "./ProductDetails";
import {ProductDescription} from "./ProductDescription";
import {ProductBreakdowns} from "./ProductBreakdowns";
import "./ProductTable.css";

interface ExternalProductTableProps {
  lotCode: string;
}

interface InternalProductTableProps {
  cellarDoor: CellarDoor | null;
}

function RetrievedProductTable(props: InternalProductTableProps & ExternalProductTableProps) {
    const { lotCode, cellarDoor } = props;
    const [wineInfo, setWineInfo] = React.useState<WineInfo | null>(null);
    const [wineNotFound, setWineNotFound] = React.useState<boolean>(false);
    const [noBackend, setNoBackend] = React.useState<boolean>(false);

    React.useEffect(() => {
        if (cellarDoor === null) {
            setNoBackend(true);
            return;
        }
        cellarDoor.getWineInfo(lotCode).then(wineInfo => {
            setWineInfo(wineInfo);
        })
        .catch(() => {
            setWineNotFound(true);
        });
    }, [cellarDoor, lotCode]);

    if (noBackend) {
        return <div>Not connected to backend</div>;
    }

    return <div className="product">
        <h1>{props.lotCode}</h1>
        {wineNotFound || wineInfo === null
            ? <p className={'subheading error'}>Wine not found</p>
            : <>
                  <ProductDescription wine={wineInfo} />
                  <ProductDetails wine={wineInfo} />
                  <ProductBreakdowns wine={wineInfo} />
              </>}
    </div>
}

export const ProductTable = (props: ExternalProductTableProps) => {
    return <CellarDoorConsumer>
        {(door) => <RetrievedProductTable {...props} cellarDoor={door}/>}
    </CellarDoorConsumer>;
}
