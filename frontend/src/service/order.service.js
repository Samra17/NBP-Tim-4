import api from "./api"
import tokenService from "./token.service";

class OrderService {

    getUserOrders() {
        try {
          return api
              .get("/order/getforuser")
              .then(response=> {
                return response;
              })
          } catch(e) {
            console.log(e)
          }
        
      }

      getAdminOrders() {
        try {
          return api
              .get("/order/adminorders")
              .then(response=> {
                return response;
              })
          } catch(e) {
            console.log(e)
          }
        
      }

      getAdminSpending() {
        try {
          return api
              .get("/order/adminspending")
              .then(response=> {
                return response;
              })
          } catch(e) {
            console.log(e)
          }
        
      }

      getAdminRestaurantRevenue() {
        try {
          return api
              .get("/order/adminrestaurantrevenue")
              .then(response=> {
                return response;
              })
          } catch(e) {
            console.log(e)
          }
        
      }

      changeOrderStatus(orderId, status) {
       return api
          .put("/order/status/" + orderId + "/" + status)
          
      }

      acceptOrderForRestaurant(orderId) {
        //promjena statusa u In preparation
        //znaci da je restoran prihvatio narudzbu
        return this.changeOrderStatus(orderId, "In preparation")
      }

      acceptOrderForCourier(orderId) {
        //promjena status u In delivery
        //znaci da je dostavljac prihvatio narudzbu za dostavu
        //posto ovo poziva courier, njegov uuid ce biti u headeru, jer je on current user
        return api
          .put("/order/adddeliveryperson/" + orderId)
          
      }

      rejectOrder(orderId) {
        //promjena statusa u Rejected
        //znaci da je restoran odbio naredbu
        return this.changeOrderStatus(orderId, "Rejected")
      }

      orderReady(orderId) {
        //promjena statusa u Ready for delivery
        //znaci da je restoran pripremio narudzbu i sad je neki od dostavljaca moze preuzeti
        return this.changeOrderStatus(orderId, "Ready for delivery")
      }

      orderDelivered(orderId) {
        //promjena statusa u Delivered
        //znaci da je narudzba dostavljena naruciocu
        return this.changeOrderStatus(orderId, "Delivered")
      }

      cancelOrder(orderId) {
        return this.changeOrderStatus(orderId,"Cancelled")
      }

      getReadyForDeliveryOrders() {
        return api.get("/order/get/readyfordelivery")
      }

      getDeliveryPersonOrders() {
        return api.get("/order/get/deliveryperson")
      }

      getRestaurantPastOrders(uuid) {
        //treba vratiti narudzbe za restoran ciji je UUID proslijeđen
        //narudzbe moraju imati status Delivered
        return api.get("/order/get/restaurant/" + uuid +"/delivered")
      }

      getRestaurantPendingOrders(uuid) {
        //treba vratiti narudzbe za restoran ciji je UUID proslijeđen
        //narudzbe moraju imati status Pending (sve novokreirane narudzbe neka imaju ovaj status) 
        return api.get("/order/get/restaurant/" + uuid +"/pending")
      }

      getRestaurantInPreparationOrders(uuid) {
        //treba vratiti narudzbe za restoran ciji je UUID proslijeđen
        //narudzbe moraju imati status In preparation
        return api.get("/order/get/restaurant/" + uuid +"/in-preparation")
      }

      getRestaurantReadyOrders(uuid) {
        //treba vratiti narudzbe za restoran ciji je UUID proslijeđen
        //narudzbe moraju imati status Ready for delivery
        return api.get("/order/get/restaurant/" + uuid +"/ready-for-delivery")
      }

      createOrder(orderCreateRequest) {
        return api.post("/order/add", orderCreateRequest)
      }

}

export default new OrderService();