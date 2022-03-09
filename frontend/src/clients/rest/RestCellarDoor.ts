import {CellarDoor} from "../CellarDoor";
import {BreakdownType} from "../types/BreakdownType";
import {WineBreakdown} from "../types/WineBreakdown";
import {WineInfo} from "../types/WineInfo";

/**
 * REST Client implementation of the Cellar Door interface.
 */
export class RestCellarDoor implements CellarDoor {
    private readonly baseURL: string;

    private buildURL(path: string): string {
        return this.baseURL + path;
    }

    constructor(baseURL: string) {
        // if the parameter has a trailing slash, remove it (Github Copilot again)
        if (baseURL.endsWith("/")) {
            baseURL = baseURL.substring(0, baseURL.length - 1);
        }
        this.baseURL = baseURL;
    }

    async getWineBreakdown(lotCode: string, breakdownType: BreakdownType): Promise<WineBreakdown> {
        const wineBreakdownResponse = await fetch(this.buildURL(`/api/breakdown/${breakdownType.key}/${lotCode}`));
        return wineBreakdownResponse.json();
    }

    async getWineInfo(lotCode: string): Promise<WineInfo> {
        const wineInfoResponse = await fetch(this.buildURL("/api/wine/" + lotCode))

        if (wineInfoResponse.status === 404) {
            throw new Error("Wine not found");
        }
        return wineInfoResponse.json();
    }
}