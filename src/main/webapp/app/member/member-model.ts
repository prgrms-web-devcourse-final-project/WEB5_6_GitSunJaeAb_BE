export class MemberDTO {

  constructor(data:Partial<MemberDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  name?: string|null;
  nickname?: string|null;
  email?: string|null;
  password?: string|null;
  loginType?: string|null;
  provider?: string|null;
  role?: string|null;
  status?: string|null;
  profileImage?: string|null;
  lastLogin?: string|null;
  createdAt?: string|null;
  updatedAt?: string|null;
  deletedAt?: string|null;

}
