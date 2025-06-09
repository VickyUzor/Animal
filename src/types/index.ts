export interface User {
  id: number;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  phone?: string;
  address?: string;
  city?: string;
  state?: string;
  zipCode?: string;
  role: 'ADOPTER' | 'SHELTER_ADMIN' | 'ADMIN';
  enabled: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface Animal {
  id: number;
  name: string;
  species: 'DOG' | 'CAT' | 'RABBIT' | 'BIRD' | 'HAMSTER' | 'GUINEA_PIG' | 'FERRET' | 'OTHER';
  breed?: string;
  age: number;
  gender: 'MALE' | 'FEMALE';
  size: 'SMALL' | 'MEDIUM' | 'LARGE' | 'EXTRA_LARGE';
  weight?: number;
  color?: string;
  description?: string;
  medicalHistory?: string;
  vaccinated: boolean;
  spayedNeutered: boolean;
  houseTrained: boolean;
  goodWithKids: boolean;
  goodWithPets: boolean;
  status: 'AVAILABLE' | 'PENDING' | 'ADOPTED' | 'NOT_AVAILABLE';
  adoptionFee: number;
  imageUrls: string[];
  createdAt: string;
  updatedAt: string;
  shelterId: number;
  shelterName: string;
  isFavorited?: boolean;
}

export interface Shelter {
  id: number;
  name: string;
  description?: string;
  address: string;
  city: string;
  state: string;
  zipCode: string;
  phone?: string;
  email?: string;
  website?: string;
  verified: boolean;
  createdAt: string;
  updatedAt: string;
  adminId?: number;
  adminName?: string;
  animalCount: number;
}

export interface Adoption {
  id: number;
  status: 'PENDING' | 'APPROVED' | 'REJECTED' | 'COMPLETED' | 'CANCELLED';
  notes?: string;
  rejectionReason?: string;
  createdAt: string;
  updatedAt: string;
  approvedAt?: string;
  completedAt?: string;
  adopterId: number;
  adopterName: string;
  adopterEmail: string;
  animalId: number;
  animalName: string;
  shelterId: number;
  shelterName: string;
}

export interface Message {
  id: number;
  subject: string;
  content: string;
  isRead: boolean;
  createdAt: string;
  senderId: number;
  senderName: string;
  recipientId: number;
  recipientName: string;
  animalId?: number;
  animalName?: string;
}

export interface Notification {
  id: number;
  title: string;
  message: string;
  type: 'ADOPTION_REQUEST' | 'ADOPTION_APPROVED' | 'ADOPTION_REJECTED' | 'MESSAGE_RECEIVED' | 'FAVORITE_ADOPTED' | 'SYSTEM_NOTIFICATION';
  isRead: boolean;
  createdAt: string;
  userId: number;
  animalId?: number;
  animalName?: string;
}

export interface AuthResponse {
  token: string;
  type: string;
  user: User;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  phone?: string;
  address?: string;
  city?: string;
  state?: string;
  zipCode?: string;
  role: 'ADOPTER' | 'SHELTER_ADMIN';
}

export interface ApiResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}