import api from "./api"
import tokenService from "./token.service";

class OrderService {

    getUserOrders(page,perPage) {
      var id = tokenService.getUserId();
        try {
          return api
              .get("/api/order/get/customer/" + id + "?page=" + page + "&perPage=" + perPage)
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
              .get("/api/admin/adminorders")
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
              .get("/api/admin/adminspending")
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
              .get("/api/admin/adminrestaurantrevenue")
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
        //promjena status u Accepted For Delivery
        //znaci da je dostavljac prihvatio narudzbu za dostavu
        //posto ovo poziva courier, njegov uuid ce biti u headeru, jer je on current user
        return api
          .put("/api/order/" + orderId + "/delivery-person/add")
          
      }

      pickUpOrder(orderId) {
        //promjena status u In Delivery
        //znaci da je dostavljac preuzeo narudzbu za dostavu
        //posto ovo poziva courier, njegov uuid ce biti u headeru, jer je on current user
        return this.changeOrderStatus(orderId, "in-delivery")
          
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

      getReadyForDeliveryOrders(page,perPage) {
        return api.get(`/api/order/get/ready-for-delivery?page=${page}&perPage=${perPage}`)
      }

      getDeliveryPersonOrders(page,perPage) {
        return api.get(`/api/order/get/delivery-person?page=${page}&perPage=${perPage}`)
      }

      getRestaurantPastOrders(page,perPage) {
      
        return api.get(`/api/order/get/restaurant/delivered?page=${page}&perPage=${perPage}`)
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