import axios from 'axios';
import { AuthResponse, LoginRequest, RegisterRequest, User, Animal, Shelter, Adoption, Message, Notification, ApiResponse } from '../types';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add auth token
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Response interceptor to handle auth errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Auth API
export const authApi = {
  login: (data: LoginRequest): Promise<AuthResponse> =>
    api.post('/auth/login', data).then(res => res.data),
  
  register: (data: RegisterRequest): Promise<AuthResponse> =>
    api.post('/auth/register', data).then(res => res.data),
};

// User API
export const userApi = {
  getProfile: (id: number): Promise<User> =>
    api.get(`/users/${id}`).then(res => res.data),
  
  updateProfile: (id: number, data: Partial<User>): Promise<User> =>
    api.put(`/users/${id}`, data).then(res => res.data),
  
  checkUsernameExists: (username: string): Promise<boolean> =>
    api.get(`/users/exists/username/${username}`).then(res => res.data),
  
  checkEmailExists: (email: string): Promise<boolean> =>
    api.get(`/users/exists/email/${email}`).then(res => res.data),
};

// Animal API
export const animalApi = {
  getAvailableAnimals: (page = 0, size = 12): Promise<ApiResponse<Animal>> =>
    api.get(`/animals/available?page=${page}&size=${size}`).then(res => res.data),
  
  getAnimalById: (id: number): Promise<Animal> =>
    api.get(`/animals/${id}`).then(res => res.data),
  
  searchAnimals: (query: string, page = 0, size = 12): Promise<ApiResponse<Animal>> =>
    api.get(`/animals/search?q=${query}&page=${page}&size=${size}`).then(res => res.data),
  
  filterAnimals: (filters: any, page = 0, size = 12): Promise<ApiResponse<Animal>> => {
    const params = new URLSearchParams({ page: page.toString(), size: size.toString() });
    Object.entries(filters).forEach(([key, value]) => {
      if (value !== null && value !== undefined && value !== '') {
        params.append(key, value.toString());
      }
    });
    return api.get(`/animals/filter?${params}`).then(res => res.data);
  },
  
  createAnimal: (data: Partial<Animal>, shelterId: number): Promise<Animal> =>
    api.post(`/animals?shelterId=${shelterId}`, data).then(res => res.data),
  
  updateAnimal: (id: number, data: Partial<Animal>): Promise<Animal> =>
    api.put(`/animals/${id}`, data).then(res => res.data),
  
  deleteAnimal: (id: number): Promise<void> =>
    api.delete(`/animals/${id}`).then(res => res.data),
  
  getAnimalsByShelterId: (shelterId: number, page = 0, size = 12): Promise<ApiResponse<Animal>> =>
    api.get(`/animals/shelter/${shelterId}?page=${page}&size=${size}`).then(res => res.data),
};

// Shelter API
export const shelterApi = {
  getAllShelters: (page = 0, size = 12): Promise<ApiResponse<Shelter>> =>
    api.get(`/shelters?page=${page}&size=${size}`).then(res => res.data),
  
  getShelterById: (id: number): Promise<Shelter> =>
    api.get(`/shelters/${id}`).then(res => res.data),
  
  getShelterByAdminId: (adminId: number): Promise<Shelter> =>
    api.get(`/shelters/admin/${adminId}`).then(res => res.data),
  
  createShelter: (data: Partial<Shelter>, adminId: number): Promise<Shelter> =>
    api.post(`/shelters?adminId=${adminId}`, data).then(res => res.data),
  
  updateShelter: (id: number, data: Partial<Shelter>): Promise<Shelter> =>
    api.put(`/shelters/${id}`, data).then(res => res.data),
  
  searchShelters: (query: string, page = 0, size = 12): Promise<ApiResponse<Shelter>> =>
    api.get(`/shelters/search?q=${query}&page=${page}&size=${size}`).then(res => res.data),
};

// Adoption API
export const adoptionApi = {
  createAdoptionRequest: (animalId: number, notes?: string): Promise<Adoption> =>
    api.post(`/adoptions?animalId=${animalId}${notes ? `&notes=${encodeURIComponent(notes)}` : ''}`).then(res => res.data),
  
  getUserAdoptions: (page = 0, size = 12): Promise<ApiResponse<Adoption>> =>
    api.get(`/adoptions/user?page=${page}&size=${size}`).then(res => res.data),
  
  getShelterAdoptions: (shelterId: number, page = 0, size = 12): Promise<ApiResponse<Adoption>> =>
    api.get(`/adoptions/shelter/${shelterId}?page=${page}&size=${size}`).then(res => res.data),
  
  getPendingAdoptions: (shelterId: number, page = 0, size = 12): Promise<ApiResponse<Adoption>> =>
    api.get(`/adoptions/shelter/${shelterId}/pending?page=${page}&size=${size}`).then(res => res.data),
  
  approveAdoption: (id: number, shelterId: number): Promise<Adoption> =>
    api.put(`/adoptions/${id}/approve?shelterId=${shelterId}`).then(res => res.data),
  
  rejectAdoption: (id: number, shelterId: number, reason: string): Promise<Adoption> =>
    api.put(`/adoptions/${id}/reject?shelterId=${shelterId}&rejectionReason=${encodeURIComponent(reason)}`).then(res => res.data),
  
  completeAdoption: (id: number, shelterId: number): Promise<Adoption> =>
    api.put(`/adoptions/${id}/complete?shelterId=${shelterId}`).then(res => res.data),
  
  cancelAdoption: (id: number): Promise<void> =>
    api.put(`/adoptions/${id}/cancel`).then(res => res.data),
};

// Favorite API
export const favoriteApi = {
  addToFavorites: (animalId: number): Promise<void> =>
    api.post(`/favorites/animal/${animalId}`).then(res => res.data),
  
  removeFromFavorites: (animalId: number): Promise<void> =>
    api.delete(`/favorites/animal/${animalId}`).then(res => res.data),
  
  getFavoriteAnimals: (page = 0, size = 12): Promise<ApiResponse<Animal>> =>
    api.get(`/favorites?page=${page}&size=${size}`).then(res => res.data),
  
  isFavorited: (animalId: number): Promise<boolean> =>
    api.get(`/favorites/animal/${animalId}/check`).then(res => res.data),
};

// Message API
export const messageApi = {
  sendMessage: (data: Partial<Message>): Promise<Message> =>
    api.post('/messages', data).then(res => res.data),
  
  getReceivedMessages: (page = 0, size = 12): Promise<ApiResponse<Message>> =>
    api.get(`/messages/received?page=${page}&size=${size}`).then(res => res.data),
  
  getSentMessages: (page = 0, size = 12): Promise<ApiResponse<Message>> =>
    api.get(`/messages/sent?page=${page}&size=${size}`).then(res => res.data),
  
  getUnreadMessages: (page = 0, size = 12): Promise<ApiResponse<Message>> =>
    api.get(`/messages/unread?page=${page}&size=${size}`).then(res => res.data),
  
  markAsRead: (id: number): Promise<Message> =>
    api.put(`/messages/${id}/read`).then(res => res.data),
  
  deleteMessage: (id: number): Promise<void> =>
    api.delete(`/messages/${id}`).then(res => res.data),
  
  getUnreadCount: (): Promise<number> =>
    api.get('/messages/unread/count').then(res => res.data),
};

// Notification API
export const notificationApi = {
  getUserNotifications: (page = 0, size = 12): Promise<ApiResponse<Notification>> =>
    api.get(`/notifications/user?page=${page}&size=${size}`).then(res => res.data),
  
  getUnreadNotifications: (page = 0, size = 12): Promise<ApiResponse<Notification>> =>
    api.get(`/notifications/unread?page=${page}&size=${size}`).then(res => res.data),
  
  markAsRead: (id: number): Promise<Notification> =>
    api.put(`/notifications/${id}/read`).then(res => res.data),
  
  deleteNotification: (id: number): Promise<void> =>
    api.delete(`/notifications/${id}`).then(res => res.data),
  
  getUnreadCount: (): Promise<number> =>
    api.get('/notifications/unread/count').then(res => res.data),
};

export default api;