import { Component, OnInit } from '@angular/core';
import { first } from 'rxjs/operators';

import { User } from '@app/_models';
import { UserService } from '@app/_services';
import { UserAccountStatement } from '@app/_models/useraccountstatement';

@Component({ templateUrl: 'admin.component.html' })
export class AdminComponent implements OnInit {
   
    loading = false;
    rows?: UserAccountStatement[];

 

    constructor(private userService: UserService) { }

    ngOnInit() {
        this.loading = true;
     
    }
}