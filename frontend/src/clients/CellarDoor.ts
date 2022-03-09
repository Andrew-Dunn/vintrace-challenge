import {WineInfo} from "./types/WineInfo";
import {BreakdownType} from "./types/BreakdownType";
import {WineBreakdown} from "./types/WineBreakdown";

/**
 * Generic interface for retrieving wine information and component breakdowns.
 * Once again this could have been named something like WineInfoClient, but
 * this is still descriptive while being a bit more fun.
 */
export interface CellarDoor {
    getWineInfo(lotCode: string): Promise<WineInfo>;
    getWineBreakdown(lotCode: string, breakdownType: BreakdownType): Promise<WineBreakdown>;
}