import api from "./api"
import tokenService from "./token.service";

class OrderService {

    getUserOrders() {
        try {
          return api
              .get("/api/order/getforuser")
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
              .get("/api/order/adminorders")
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
              .get("/api/order/adminspending")
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
              .get("/api/order/adminrestaurantrevenue")
              .then(response=> {
                return response;
              })
          } catch(e) {
            console.log(e)
          }
        
      }

      changeOrderStatus(orderId, status) {
       return api
          .put("/api/order/status/" + orderId + "/" + status)
          
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
          .put("/api/order/adddeliveryperson/" + orderId)
          
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
        return api.get("/api/order/get/readyfordelivery")
      }

      getDeliveryPersonOrders() {
        return api.get("/api/order/get/deliveryperson")
      }

      getRestaurantPastOrders(page,perPage) {
      
        return api.get("/api/order/get/restaurant/delivered?page=${page}&perPage=${perPage}")
      }

      getRestaurantPendingOrders(page,perPage) {
       
        return api.get(`/api/order/get/restaurant/pending?page=${page}&perPage=${perPage}`)
      }

      getRestaurantInPreparationOrders(page,perPage) {
        
        return api.get(`/api/order/get/restaurant/in-preparation?page=${page}&perPage=${perPage}`)
      }

      getRestaurantReadyOrders(page,perPage) {
        
        return api.get(`/api/order/get/restaurant/ready-for-delivery?page=${page}&perPage=${perPage}`)
      }

      createOrder(orderCreateRequest) {
        return api.post("/api/order/add", orderCreateRequest)
      }

}

export default new OrderService();