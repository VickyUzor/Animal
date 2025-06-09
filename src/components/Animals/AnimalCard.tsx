import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { Heart, MapPin, Calendar, DollarSign } from 'lucide-react';
import { Animal } from '../../types';
import { favoriteApi } from '../../services/api';
import { useAuth } from '../../context/AuthContext';
import toast from 'react-hot-toast';

interface AnimalCardProps {
  animal: Animal;
  onFavoriteChange?: () => void;
}

const AnimalCard: React.FC<AnimalCardProps> = ({ animal, onFavoriteChange }) => {
  const { isAuthenticated } = useAuth();
  const [isFavorited, setIsFavorited] = useState(animal.isFavorited || false);
  const [isLoading, setIsLoading] = useState(false);

  const handleFavoriteToggle = async (e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();

    if (!isAuthenticated) {
      toast.error('Please login to add favorites');
      return;
    }

    setIsLoading(true);
    try {
      if (isFavorited) {
        await favoriteApi.removeFromFavorites(animal.id);
        setIsFavorited(false);
        toast.success('Removed from favorites');
      } else {
        await favoriteApi.addToFavorites(animal.id);
        setIsFavorited(true);
        toast.success('Added to favorites');
      }
      onFavoriteChange?.();
    } catch (error) {
      toast.error('Failed to update favorites');
    } finally {
      setIsLoading(false);
    }
  };

  const getSpeciesEmoji = (species: string) => {
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

  const defaultImage = `https://images.pexels.com/photos/1108099/pexels-photo-1108099.jpeg?auto=compress&cs=tinysrgb&w=400`;
  const imageUrl = animal.imageUrls && animal.imageUrls.length > 0 ? animal.imageUrls[0] : defaultImage;

  return (
    <Link to={`/animals/${animal.id}`} className="block">
      <div className="bg-white rounded-xl shadow-md hover:shadow-xl transition-all duration-300 transform hover:-translate-y-1 overflow-hidden">
        <div className="relative">
          <img
            src={imageUrl}
            alt={animal.name}
            className="w-full h-48 object-cover"
            onError={(e) => {
              (e.target as HTMLImageElement).src = defaultImage;
            }}
          />
          <button
            onClick={handleFavoriteToggle}
            disabled={isLoading}
            className={`absolute top-3 right-3 p-2 rounded-full transition-all duration-200 ${
              isFavorited
                ? 'bg-red-500 text-white'
                : 'bg-white/80 text-gray-600 hover:bg-white'
            } ${isLoading ? 'opacity-50 cursor-not-allowed' : ''}`}
          >
            <Heart className={`w-4 h-4 ${isFavorited ? 'fill-current' : ''}`} />
          </button>
          <div className="absolute top-3 left-3 bg-blue-600 text-white px-2 py-1 rounded-full text-xs font-medium">
            {getSpeciesEmoji(animal.species)} {animal.species.replace('_', ' ')}
          </div>
        </div>

        <div className="p-4">
          <div className="flex justify-between items-start mb-2">
            <h3 className="text-lg font-semibold text-gray-900 truncate">{animal.name}</h3>
            <span className="text-sm text-gray-500 ml-2">{animal.gender}</span>
          </div>

          <div className="space-y-2 mb-3">
            {animal.breed && (
              <p className="text-sm text-gray-600 truncate">{animal.breed}</p>
            )}
            
            <div className="flex items-center text-sm text-gray-500">
              <Calendar className="w-4 h-4 mr-1" />
              <span>{animal.age} year{animal.age !== 1 ? 's' : ''} old</span>
            </div>

            <div className="flex items-center text-sm text-gray-500">
              <MapPin className="w-4 h-4 mr-1" />
              <span className="truncate">{animal.shelterName}</span>
            </div>

            <div className="flex items-center text-sm text-green-600 font-medium">
              <DollarSign className="w-4 h-4 mr-1" />
              <span>${animal.adoptionFee}</span>
            </div>
          </div>

          {animal.description && (
            <p className="text-sm text-gray-600 line-clamp-2 mb-3">
              {animal.description}
            </p>
          )}

          <div className="flex flex-wrap gap-1 mb-3">
            {animal.vaccinated && (
              <span className="px-2 py-1 bg-green-100 text-green-800 text-xs rounded-full">
                Vaccinated
              </span>
            )}
            {animal.spayedNeutered && (
              <span className="px-2 py-1 bg-blue-100 text-blue-800 text-xs rounded-full">
                Spayed/Neutered
              </span>
            )}
            {animal.houseTrained && (
              <span className="px-2 py-1 bg-purple-100 text-purple-800 text-xs rounded-full">
                House Trained
              </span>
            )}
            {animal.goodWithKids && (
              <span className="px-2 py-1 bg-yellow-100 text-yellow-800 text-xs rounded-full">
                Good with Kids
              </span>
            )}
          </div>

          <div className="flex justify-between items-center">
            <span className={`px-2 py-1 rounded-full text-xs font-medium ${
              animal.status === 'AVAILABLE'
                ? 'bg-green-100 text-green-800'
                : animal.status === 'PENDING'
                ? 'bg-yellow-100 text-yellow-800'
                : 'bg-gray-100 text-gray-800'
            }`}>
              {animal.status.replace('_', ' ')}
            </span>
            <span className="text-xs text-gray-500">{animal.size}</span>
          </div>
        </div>
      </div>
    </Link>
  );
};

export default AnimalCard;