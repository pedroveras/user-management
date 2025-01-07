import { Profile } from "./profile";

export interface User {
  id: string;
  username: string;
  name: string;
  birthDate: string;
  phone: string;
  email: string;
  profile: Profile;
}
