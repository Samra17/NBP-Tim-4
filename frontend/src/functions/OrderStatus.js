
export function orderStatus(status) {
    switch(status) {
        case "NEW":
            return <span style={{fontWeight:"bold"}}  className="text-info">Pending</span>
        case "ACCEPTED":
            return <span style={{color:"#FE724C",fontWeight:"bold"}}>In preparation</span>

        case "READY_FOR_DELIVERY":
            return <span style={{fontWeight:"bold"}} className="text-warning">Ready for delivery</span>

        case "REJECTED":
            return <span style={{fontWeight:"bold"}} className="text-danger">Rejected</span>

        case "CANCELLED":
            return <span style={{fontWeight:"bold"}} className="text-danger">Cancelled</span>

        case "ACCEPTED_FOR_DELIVERY":
            return <span style={{fontWeight:"bold"}} className="text-warning">Accepted for delivery</span>

        case "IN_DELIVERY":
            return <span style={{fontWeight:"bold"}} className="text-info">In delivery</span>

        case "DELIVERED":
            return <span style={{fontWeight:"bold"}} className="text-success">Delivered</span>
    }
}