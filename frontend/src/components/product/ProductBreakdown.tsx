import {BreakdownType} from "../../clients/types/BreakdownType";
import {WineInfo} from "../../clients/types/WineInfo";
import {CellarDoor} from "../../clients/CellarDoor";
import { CellarDoorConsumer } from "../CellarDoorContext";
import {WineBreakdownComponent} from "../../clients/types/WineBreakdown";
import React from "react";

function RetrievedProductBreakdown(props: {
    wineInfo: WineInfo;
    breakdownType: BreakdownType;
    door: CellarDoor;
}) {
    const [components, setComponents] = React.useState<WineBreakdownComponent[]>([]);
    const [limitComponents, setLimitComponents] = React.useState<boolean>(true);

    React.useEffect(() => {
        props.door.getWineBreakdown(props.wineInfo.lotCode, props.breakdownType).then(breakdown => {
            // Whenever the breakdown changes, the table should be re-limited to displaying the first 5 components.
            setLimitComponents(true);
            setComponents(breakdown.breakdown);
        });
    }, [props.wineInfo.lotCode, props.breakdownType]);

    return (
        <div className="product-breakdown">
            <table>
                <thead>
                    <tr>
                        <th scope="col" className="category">{props.breakdownType.humanReadable}</th>
                        <th scope="col" className="percentage">Percentage</th>
                    </tr>
                </thead>
                <tbody>
                    {components.slice(0, limitComponents ? 5 : components.length).map((component, index) => (
                        <tr key={index}>
                            <th scope="row" className="category">{component.key}</th>
                            <td className="percentage">{component.percentage}%</td>
                        </tr>
                    ))}
                </tbody>
            </table>
            {limitComponents && components.length > 5 && (
                <button className="show-more" onClick={() => setLimitComponents(false)}>Show more</button>
            )}
        </div>
    );
}

export function ProductBreakdown(props: { breakdown: BreakdownType, wine: WineInfo }) {
    return <CellarDoorConsumer>
        {(door) => door && <RetrievedProductBreakdown wineInfo={props.wine} breakdownType={props.breakdown} door={door}/>}
    </CellarDoorConsumer>
}