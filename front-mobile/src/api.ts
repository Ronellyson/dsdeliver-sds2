import axios from "axios";

const API_URL = 'https://ronellyson-sds2.herokuapp.com';

export function fetchOrders() {
  return axios(`${API_URL}/orders`);
}

export function confirmDelivery(ordeId: number) {
  return axios.put(`${API_URL}/orders/${ordeId}/delivered`); 
}