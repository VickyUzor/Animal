import React, { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import { animalApi } from '../../services/api';
import { Animal, ApiResponse } from '../../types';
import AnimalCard from '../../components/Animals/AnimalCard';
import AnimalFilters from '../../components/Animals/AnimalFilters';
import LoadingSpinner from '../../components/Common/LoadingSpinner';
import Pagination from '../../components/Common/Pagination';
import { Search } from 'lucide-react';
import toast from 'react-hot-toast';

interface FilterState {
  species: string;
  breed: string;
  size: string;
  gender: string;
  minAge: string;
  maxAge: string;
  goodWithKids: string;
  goodWithPets: string;
  houseTrained: string;
}

const AnimalList: React.FC = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [animals, setAnimals] = useState<Animal[]>([]);
  const [loading, setLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [searchQuery, setSearchQuery] = useState(searchParams.get('search') || '');
  const [filtersOpen, setFiltersOpen] = useState(false);
  const [filters, setFilters] = useState<FilterState>({
    species: '',
    breed: '',
    size: '',
    gender: '',
    minAge: '',
    maxAge: '',
    goodWithKids: '',
    goodWithPets: '',
    houseTrained: ''
  });

  const fetchAnimals = async (page = 0) => {
    setLoading(true);
    try {
      let response: ApiResponse<Animal>;

      if (searchQuery.trim()) {
        response = await animalApi.searchAnimals(searchQuery.trim(), page);
      } else if (Object.values(filters).some(value => value !== '')) {
        const filterParams = Object.entries(filters).reduce((acc, [key, value]) => {
          if (value !== '') {
            acc[key] = value === 'true' ? true : value === 'false' ? false : value;
          }
          return acc;
        }, {} as any);
        response = await animalApi.filterAnimals(filterParams, page);
      } else {
        response = await animalApi.getAvailableAnimals(page);
      }

      setAnimals(response.content);
      setTotalPages(response.totalPages);
      setCurrentPage(page);
    } catch (error) {
      toast.error('Failed to fetch animals');
      console.error('Error fetching animals:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAnimals(0);
  }, [searchQuery, filters]);

  useEffect(() => {
    const search = searchParams.get('search');
    if (search) {
      setSearchQuery(search);
    }
  }, [searchParams]);

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    if (searchQuery.trim()) {
      setSearchParams({ search: searchQuery.trim() });
    } else {
      setSearchParams({});
    }
    setCurrentPage(0);
  };

  const handleFilterChange = (newFilters: FilterState) => {
    setFilters(newFilters);
    setCurrentPage(0);
  };

  const handleClearFilters = () => {
    setFilters({
      species: '',
      breed: '',
      size: '',
      gender: '',
      minAge: '',
      maxAge: '',
      goodWithKids: '',
      goodWithPets: '',
      houseTrained: ''
    });
    setSearchQuery('');
    setSearchParams({});
    setCurrentPage(0);
  };

  const handlePageChange = (page: number) => {
    fetchAnimals(page);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900 mb-4">Find Your Perfect Pet</h1>
          
          {/* Search Bar */}
          <form onSubmit={handleSearch} className="mb-6">
            <div className="relative max-w-md">
              <input
                type="text"
                placeholder="Search animals..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              />
              <Search className="absolute left-3 top-3.5 h-5 w-5 text-gray-400" />
            </div>
          </form>

          {/* Filters */}
          <AnimalFilters
            filters={filters}
            onFilterChange={handleFilterChange}
            onClearFilters={handleClearFilters}
            isOpen={filtersOpen}
            onToggle={() => setFiltersOpen(!filtersOpen)}
          />
        </div>

        {/* Results */}
        {loading ? (
          <div className="flex justify-center items-center py-12">
            <LoadingSpinner size="lg" />
          </div>
        ) : (
          <>
            <div className="mb-6">
              <p className="text-gray-600">
                {animals.length === 0 ? 'No animals found' : `${animals.length} animals found`}
                {searchQuery && ` for "${searchQuery}"`}
              </p>
            </div>

            {animals.length === 0 ? (
              <div className="text-center py-12">
                <div className="w-24 h-24 bg-gray-200 rounded-full flex items-center justify-center mx-auto mb-4">
                  <Search className="w-12 h-12 text-gray-400" />
                </div>
                <h3 className="text-xl font-semibold text-gray-900 mb-2">No animals found</h3>
                <p className="text-gray-600 mb-4">
                  Try adjusting your search criteria or filters to find more animals.
                </p>
                <button
                  onClick={handleClearFilters}
                  className="bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700 transition-colors"
                >
                  Clear Filters
                </button>
              </div>
            ) : (
              <>
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 mb-8">
                  {animals.map((animal) => (
                    <AnimalCard
                      key={animal.id}
                      animal={animal}
                      onFavoriteChange={() => fetchAnimals(currentPage)}
                    />
                  ))}
                </div>

                <Pagination
                  currentPage={currentPage}
                  totalPages={totalPages}
                  onPageChange={handlePageChange}
                  className="mb-8"
                />
              </>
            )}
          </>
        )}
      </div>
    </div>
  );
};

export default AnimalList;