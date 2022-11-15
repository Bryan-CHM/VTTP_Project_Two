export interface User {
  email: string
  password: string
  admin?: boolean
}

export interface Book {
  title?: string
  authors?: string
  description?: string
  thumbnail?: string
  publisher?: string
  publishedDate?: string
  pageCount?: string
  quantity?: number
  duration?: string
  uploadImage? : any
}

export interface LoginResponse{
  status: boolean
  admin: boolean
}

export interface CartResponse{
  status: boolean
}
