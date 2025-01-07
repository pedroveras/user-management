export interface ApiResponse<T> {
    data: T;
    message?: string;
    status?: string; // Optional fields based on your API response
  }
  