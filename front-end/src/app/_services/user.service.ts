import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';

import { environment } from '@environments/environment';
import { User } from '@app/_models';
import { UserAccountStatement } from '@app/_models/useraccountstatement';


@Injectable({ providedIn: 'root' })
export class UserService {
    constructor(private http: HttpClient) { }

    getAll(accountId: string,fromDate: string,toDate: string,startAmount: number,endAmount: number) {
        let queryParams = new HttpParams();
        queryParams = queryParams.append("accountId", accountId);
        queryParams = queryParams.append("fromDate",fromDate);
        queryParams = queryParams.append("toDate",toDate);
        queryParams = queryParams.append("startAmount",startAmount);
        queryParams = queryParams.append("endAmount",endAmount);
        return this.http.get<UserAccountStatement[]>(`${environment.apiUrl}/account-ws/account/statement`,{params:queryParams});
    }

    getById(id: string) {
        return this.http.get<UserAccountStatement[]>(`${environment.apiUrl}/account-ws/account/${id}/statement`);
    }
}