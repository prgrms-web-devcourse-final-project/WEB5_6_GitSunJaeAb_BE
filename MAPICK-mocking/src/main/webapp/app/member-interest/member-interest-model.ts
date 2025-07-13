export class MemberInterestDTO {

  constructor(data:Partial<MemberInterestDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  createdAt?: string|null;
  interest?: number|null;
  member?: number|null;

}
