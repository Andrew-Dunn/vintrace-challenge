import {ProductTable} from "../components/product/ProductTable";
import {useParams} from "react-router-dom";

export default function WinePage() {
  let params = useParams();
  return (
    <div>
      <ProductTable lotCode={params.lotCode!}/>
    </div>
  );
}