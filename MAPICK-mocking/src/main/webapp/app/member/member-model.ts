export class MemberDTO {

  constructor(data:Partial<MemberDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  blacklisted?: boolean|null;
  name?: string|null;
  nickname?: string|null;
  email?: string|null;
  role?: string|null;
  // password?: string|null;
  // loginType?: string|null;
  // provider?: string|null;
  // status?: string|null;
  // profileImage?: string|null;
  // lastLogin?: string|null;
  // createdAt?: string|null;
  // updatedAt?: string|null;
  // deletedAt?: string|null;

}
