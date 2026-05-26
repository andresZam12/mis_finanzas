export interface AuthRequest {
  username: string;
  password: string;
}

export interface RegistroRequest {
  username: string;
  password: string;
  nombre: string;
}

export interface AuthResponse {
  token: string;
  refreshToken: string;
  usuarioId: number;
  username: string;
}
