export const BreakdownType = {
    YEAR: { key: "year", humanReadable: "Year" },
    VARIETY: { key: "variety", humanReadable: "Variety" },
    REGION: { key: "region", humanReadable: "Region" },
    YEAR_VARIETY: { key: "year-variety", humanReadable: "Year & Variety" },
    REGION_VARIETY: { key: "region-variety", humanReadable: "Region & Variety" },
} as const;

// Bit of a hack to create an enum-like type with complex values.
export type BreakdownType = typeof BreakdownType[keyof typeof BreakdownType]
