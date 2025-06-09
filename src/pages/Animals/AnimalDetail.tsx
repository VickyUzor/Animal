import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { animalApi, adoptionApi, favoriteApi, messageApi } from '../../services/api';
import { Animal } from '../../types';
import { useAuth } from '../../context/AuthContext';
import LoadingSpinner from '../../components/Common/LoadingSpinner';
import { 
  Heart, 
  MapPin, 
  Calendar, 
  DollarSign, 
  Mail, 
  ArrowLeft,
  Check,
  X,
  Info
} from 'lucide-react';
import toast from 'react-hot-toast';

const AnimalDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { user, isAuthenticated } = useAuth();
  const [animal, setAnimal] = useState<Animal | null>(null);
  const [loading, setLoading] = useState(true);
  const [isFavorited, setIsFavorited] = useState(false);
  const [showAdoptionModal, setShowAdoptionModal] = useState(false);
  const [showMessageModal, setShowMessageModal] = useState(false);
  const [adoptionNotes, setAdoptionNotes] = useState('');
  const [messageSubject, setMessageSubject] = useState('');
  const [messageContent, setMessageContent] = useState('');
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    if (id) {
      fetchAnimal();
    }
  }, [id]);

  const fetchAnimal = async () => {
    try {
      const animalData = await animalApi.getAnimalById(Number(id));
      setAnimal(animalData);
      setIsFavorited(animalData.isFavorited || false);
      setMessageSubject(`Inquiry about ${animalData.name}`);
    } catch (error) {
      toast.error('Failed to fetch animal details');
      navigate('/animals');
    } finally {
      setLoading(false);
    }
  };

  const handleFavoriteToggle = async () => {
    if (!isAuthenticated) {
      toast.error('Please login to add favorites');
      return;
    }

    try {
      if (isFavorited) {
        await favoriteApi.removeFromFavorites(animal!.id);
        setIsFavorited(false);
        toast.success('Removed from favorites');
      } else {
        await favoriteApi.addToFavorites(animal!.id);
        setIsFavorited(true);
        toast.success('Added to favorites');
      }
    } catch (error) {
      toast.error('Failed to update favorites');
    }
  };

  const handleAdoptionRequest = async () => {
    if (!isAuthenticated) {
      toast.error('Please login to request adoption');
      return;
    }

    setSubmitting(true);
    try {
      await adoptionApi.createAdoptionRequest(animal!.id, adoptionNotes);
      toast.success('Adoption request submitted successfully!');
      setShowAdoptionModal(false);
      setAdoptionNotes('');
      fetchAnimal(); // Refresh to update status
    } catch (error: any) {
      toast.error(error.response?.data?.message || 'Failed to submit adoption request');
    } finally {
      setSubmitting(false);
    }
  };

  const handleSendMessage = async () => {
    if (!isAuthenticated) {
      toast.error('Please login to send messages');
      return;
    }

    if (!messageSubject.trim() || !messageContent.trim()) {
      toast.error('Please fill in all fields');
      return;
    }

    setSubmitting(true);
    try {
      // For now, we'll assume the shelter admin is the recipient
      // In a real app, you'd get the shelter admin ID from the animal data
      await messageApi.sendMessage({
        subject: messageSubject,
        content: messageContent,
        recipientId: 1, // This should be the shelter admin ID
        animalId: animal!.id
      });
      toast.success('Message sent successfully!');
      setShowMessageModal(false);
      setMessageContent('');
    } catch (error: any) {
      toast.error(error.response?.data?.message || 'Failed to send message');
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <LoadingSpinner size="lg" />
      </div>
    );
  }

  if (!animal) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-center">
          <h2 className="text-2xl font-bold text-gray-900 mb-4">Animal not found</h2>
          <button
            onClick={() => navigate('/animals')}
            className="bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700 transition-colors"
          >
            Back to Animals
          </button>
        </div>
      </div>
    );
  }

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

  const defaultImage = `https://images.pexels.com/photos/1108099/pexels-photo-1108099.jpeg?auto=compress&cs=tinysrgb&w=800`;
  const imageUrl = animal.imageUrls && animal.imageUrls.length > 0 ? animal.imageUrls[0] : defaultImage;

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Back Button */}
        <button
          onClick={() => navigate('/animals')}
          className="flex items-center space-x-2 text-gray-600 hover:text-blue-600 transition-colors mb-6"
        >
          <ArrowLeft className="w-5 h-5" />
          <span>Back to Animals</span>
        </button>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          {/* Image Section */}
          <div className="space-y-4">
            <div className="relative">
              <img
                src={imageUrl}
                alt={animal.name}
                className="w-full h-96 object-cover rounded-xl shadow-lg"
                onError={(e) => {
                  (e.target as HTMLImageElement).src = defaultImage;
                }}
              />
              <button
                onClick={handleFavoriteToggle}
                className={`absolute top-4 right-4 p-3 rounded-full transition-all duration-200 ${
                  isFavorited
                    ? 'bg-red-500 text-white'
                    : 'bg-white/80 text-gray-600 hover:bg-white'
                }`}
              >
                <Heart className={`w-6 h-6 ${isFavorited ? 'fill-current' : ''}`} />
              </button>
              <div className="absolute top-4 left-4 bg-blue-600 text-white px-3 py-2 rounded-full text-sm font-medium">
                {getSpeciesEmoji(animal.species)} {animal.species.replace('_', ' ')}
              </div>
            </div>
          </div>

          {/* Details Section */}
          <div className="space-y-6">
            <div>
              <div className="flex items-center justify-between mb-2">
                <h1 className="text-3xl font-bold text-gray-900">{animal.name}</h1>
                <span className={`px-3 py-1 rounded-full text-sm font-medium ${
                  animal.status === 'AVAILABLE'
                    ? 'bg-green-100 text-green-800'
                    : animal.status === 'PENDING'
                    ? 'bg-yellow-100 text-yellow-800'
                    : 'bg-gray-100 text-gray-800'
                }`}>
                  {animal.status.replace('_', ' ')}
                </span>
              </div>
              
              {animal.breed && (
                <p className="text-xl text-gray-600 mb-4">{animal.breed}</p>
              )}

              <div className="grid grid-cols-2 gap-4 mb-6">
                <div className="flex items-center text-gray-600">
                  <Calendar className="w-5 h-5 mr-2" />
                  <span>{animal.age} year{animal.age !== 1 ? 's' : ''} old</span>
                </div>
                <div className="flex items-center text-gray-600">
                  <span className="mr-2">⚧</span>
                  <span>{animal.gender}</span>
                </div>
                <div className="flex items-center text-gray-600">
                  <span className="mr-2">📏</span>
                  <span>{animal.size.replace('_', ' ')}</span>
                </div>
                {animal.weight && (
                  <div className="flex items-center text-gray-600">
                    <span className="mr-2">⚖️</span>
                    <span>{animal.weight} lbs</span>
                  </div>
                )}
              </div>

              <div className="flex items-center text-green-600 font-semibold text-xl mb-6">
                <DollarSign className="w-6 h-6 mr-1" />
                <span>${animal.adoptionFee} adoption fee</span>
              </div>

              <div className="flex items-center text-gray-600 mb-6">
                <MapPin className="w-5 h-5 mr-2" />
                <span>{animal.shelterName}</span>
              </div>
            </div>

            {/* Characteristics */}
            <div>
              <h3 className="text-lg font-semibold text-gray-900 mb-3">Characteristics</h3>
              <div className="grid grid-cols-2 gap-3">
                <div className="flex items-center space-x-2">
                  {animal.vaccinated ? (
                    <Check className="w-5 h-5 text-green-600" />
                  ) : (
                    <X className="w-5 h-5 text-red-600" />
                  )}
                  <span className="text-gray-700">Vaccinated</span>
                </div>
                <div className="flex items-center space-x-2">
                  {animal.spayedNeutered ? (
                    <Check className="w-5 h-5 text-green-600" />
                  ) : (
                    <X className="w-5 h-5 text-red-600" />
                  )}
                  <span className="text-gray-700">Spayed/Neutered</span>
                </div>
                <div className="flex items-center space-x-2">
                  {animal.houseTrained ? (
                    <Check className="w-5 h-5 text-green-600" />
                  ) : (
                    <X className="w-5 h-5 text-red-600" />
                  )}
                  <span className="text-gray-700">House Trained</span>
                </div>
                <div className="flex items-center space-x-2">
                  {animal.goodWithKids ? (
                    <Check className="w-5 h-5 text-green-600" />
                  ) : (
                    <X className="w-5 h-5 text-red-600" />
                  )}
                  <span className="text-gray-700">Good with Kids</span>
                </div>
                <div className="flex items-center space-x-2">
                  {animal.goodWithPets ? (
                    <Check className="w-5 h-5 text-green-600" />
                  ) : (
                    <X className="w-5 h-5 text-red-600" />
                  )}
                  <span className="text-gray-700">Good with Pets</span>
                </div>
              </div>
            </div>

            {/* Description */}
            {animal.description && (
              <div>
                <h3 className="text-lg font-semibold text-gray-900 mb-3">About {animal.name}</h3>
                <p className="text-gray-700 leading-relaxed">{animal.description}</p>
              </div>
            )}

            {/* Medical History */}
            {animal.medicalHistory && (
              <div>
                <h3 className="text-lg font-semibold text-gray-900 mb-3">Medical History</h3>
                <p className="text-gray-700 leading-relaxed">{animal.medicalHistory}</p>
              </div>
            )}

            {/* Action Buttons */}
            {animal.status === 'AVAILABLE' && (
              <div className="flex flex-col sm:flex-row gap-4">
                <button
                  onClick={() => setShowAdoptionModal(true)}
                  className="flex-1 bg-blue-600 text-white px-6 py-3 rounded-lg font-semibold hover:bg-blue-700 transition-colors"
                >
                  Request Adoption
                </button>
                <button
                  onClick={() => setShowMessageModal(true)}
                  className="flex-1 border border-blue-600 text-blue-600 px-6 py-3 rounded-lg font-semibold hover:bg-blue-50 transition-colors flex items-center justify-center"
                >
                  <Mail className="w-5 h-5 mr-2" />
                  Contact Shelter
                </button>
              </div>
            )}

            {animal.status === 'PENDING' && (
              <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4">
                <div className="flex items-center">
                  <Info className="w-5 h-5 text-yellow-600 mr-2" />
                  <span className="text-yellow-800 font-medium">
                    This animal has a pending adoption request
                  </span>
                </div>
              </div>
            )}

            {animal.status === 'ADOPTED' && (
              <div className="bg-gray-50 border border-gray-200 rounded-lg p-4">
                <div className="flex items-center">
                  <Heart className="w-5 h-5 text-gray-600 mr-2" />
                  <span className="text-gray-800 font-medium">
                    This animal has been adopted
                  </span>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>

      {/* Adoption Modal */}
      {showAdoptionModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-lg max-w-md w-full p-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">
              Request Adoption for {animal.name}
            </h3>
            <div className="mb-4">
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Additional Notes (Optional)
              </label>
              <textarea
                value={adoptionNotes}
                onChange={(e) => setAdoptionNotes(e.target.value)}
                placeholder="Tell the shelter why you'd be a great match for this pet..."
                rows={4}
                className="w-full border border-gray-300 rounded-lg px-3 py-2 focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              />
            </div>
            <div className="flex space-x-3">
              <button
                onClick={() => setShowAdoptionModal(false)}
                className="flex-1 border border-gray-300 text-gray-700 px-4 py-2 rounded-lg hover:bg-gray-50 transition-colors"
              >
                Cancel
              </button>
              <button
                onClick={handleAdoptionRequest}
                disabled={submitting}
                className="flex-1 bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors disabled:opacity-50"
              >
                {submitting ? 'Submitting...' : 'Submit Request'}
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Message Modal */}
      {showMessageModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-lg max-w-md w-full p-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">
              Contact Shelter about {animal.name}
            </h3>
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Subject
                </label>
                <input
                  type="text"
                  value={messageSubject}
                  onChange={(e) => setMessageSubject(e.target.value)}
                  className="w-full border border-gray-300 rounded-lg px-3 py-2 focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Message
                </label>
                <textarea
                  value={messageContent}
                  onChange={(e) => setMessageContent(e.target.value)}
                  placeholder="Your message to the shelter..."
                  rows={4}
                  className="w-full border border-gray-300 rounded-lg px-3 py-2 focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
              </div>
            </div>
            <div className="flex space-x-3 mt-6">
              <button
                onClick={() => setShowMessageModal(false)}
                className="flex-1 border border-gray-300 text-gray-700 px-4 py-2 rounded-lg hover:bg-gray-50 transition-colors"
              >
                Cancel
              </button>
              <button
                onClick={handleSendMessage}
                disabled={submitting}
                className="flex-1 bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors disabled:opacity-50"
              >
                {submitting ? 'Sending...' : 'Send Message'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default AnimalDetail;