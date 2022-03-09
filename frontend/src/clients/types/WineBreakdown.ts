import {BreakdownType} from "./BreakdownType";

export interface WineBreakdown {
    breakDownType: BreakdownType["key"];
    breakdown: WineBreakdownComponent[];
}

export interface WineBreakdownComponent {
    percentage: string;
    key: string;
}