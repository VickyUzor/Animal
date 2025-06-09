import { format, formatDistanceToNow } from 'date-fns';

export const formatDate = (date: string | Date) => {
  return format(new Date(date), 'MMM dd, yyyy');
};

export const formatDateTime = (date: string | Date) => {
  return format(new Date(date), 'MMM dd, yyyy HH:mm');
};

export const formatRelativeTime = (date: string | Date) => {
  return formatDistanceToNow(new Date(date), { addSuffix: true });
};

export const formatCurrency = (amount: number) => {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
  }).format(amount);
};

export const truncateText = (text: string, maxLength: number) => {
  if (text.length <= maxLength) return text;
  return text.slice(0, maxLength) + '...';
};

export const getInitials = (firstName: string, lastName: string) => {
  return `${firstName.charAt(0)}${lastName.charAt(0)}`.toUpperCase();
};

export const validateEmail = (email: string) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
};

export const validatePhone = (phone: string) => {
  const phoneRegex = /^\(?([0-9]{3})\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})$/;
  return phoneRegex.test(phone);
};

export const formatPhoneNumber = (phone: string) => {
  const cleaned = phone.replace(/\D/g, '');
  const match = cleaned.match(/^(\d{3})(\d{3})(\d{4})$/);
  if (match) {
    return `(${match[1]}) ${match[2]}-${match[3]}`;
  }
  return phone;
};

export const getStatusColor = (status: string) => {
  const colors: { [key: string]: string } = {
    AVAILABLE: 'green',
    PENDING: 'yellow',
    ADOPTED: 'blue',
    NOT_AVAILABLE: 'gray',
    APPROVED: 'blue',
    REJECTED: 'red',
    COMPLETED: 'green',
    CANCELLED: 'gray'
  };
  return colors[status] || 'gray';
};

export const getSpeciesEmoji = (species: string) => {
  const emojis: { [key: string]: string } = {
    DOG: '🐕',
    CAT: '🐱',
    RABBIT: '🐰',
    BIRD: '🐦',
    HAMSTER: '🐹',
    GUINEA_PIG: '🐹',
    FERRET: '🦔',
    OTHER: '🐾'
  };
  return emojis[species] || '🐾';
};

export const debounce = <T extends (...args: any[]) => any>(
  func: T,
  wait: number
): ((...args: Parameters<T>) => void) => {
  let timeout: NodeJS.Timeout;
  return (...args: Parameters<T>) => {
    clearTimeout(timeout);
    timeout = setTimeout(() => func(...args), wait);
  };
};

export const generateImagePlaceholder = (width: number, height: number, text?: string) => {
  return `https://via.placeholder.com/${width}x${height}/e5e7eb/6b7280?text=${encodeURIComponent(text || 'Image')}`;
};