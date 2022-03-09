import {WineInfo} from "../../clients/types/WineInfo";
import {BreakdownType} from "../../clients/types/BreakdownType";
import React from "react";
import {ProductBreakdown} from "./ProductBreakdown";

export function ProductBreakdowns(props: {wine: WineInfo})
{
    const {wine} = props;
    const [selectedBreakdown, selectBreakdown] = React.useState<BreakdownType>(BreakdownType.YEAR)
    const breakdowns = Object.keys(BreakdownType) as (keyof typeof BreakdownType)[];

    return (
        <div className="product-breakdowns">
            <div className="breakdown-selector">
                {breakdowns.map(b =>
                    <button key={`breakdown_${b}`}
                            className={selectedBreakdown.key === BreakdownType[b].key ? "selected" : ""}
                            onClick={() => {
                                if (selectedBreakdown.key !== BreakdownType[b].key) {
                                    return selectBreakdown(BreakdownType[b])
                                }
                            }}>
                        {BreakdownType[b].humanReadable}
                    </button>
                )}
            </div>
            <ProductBreakdown wine={wine} breakdown={selectedBreakdown} />
        </div>
    );
}