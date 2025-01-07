import {User} from "./user";

export interface Profile {
  id: string;
  name: string;
  description: string;
  type: string;
  // users: User[];
}
