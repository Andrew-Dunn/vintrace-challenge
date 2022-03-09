import {WineInfo} from "../../clients/types/WineInfo";
import React from "react";

interface ProductDescriptionProps {
    wine: WineInfo | null;
}

export function ProductDescription(props: ProductDescriptionProps) {
    const {wine} = props;
    if (!wine) {
        return null;
    }
    return (
        <p className={'subheading product-description'}>{wine.description}</p>
    );
}