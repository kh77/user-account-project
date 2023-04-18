import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';
import { first } from 'rxjs/operators';

import { Observable } from 'rxjs';

import { User } from '@app/_models';
import { UserService, AuthenticationService } from '@app/_services';
import { UserAccountStatement } from '@app/_models/useraccountstatement';
import { HttpClient } from '@angular/common/http';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';




@Component({ templateUrl: 'home.component.html' ,styles:[`

:host ::ng-deep .datatable-body-cell {
  padding: 0 !important;
}
:host ::ng-deep .datatable-body-cell-label {
  text-align: "center" !important;
  margin-left: 27px !important;;

}
`]})
export class HomeComponent {
    loading = false;
    user: User;
    role: string;
    rows?: UserAccountStatement[];
    columns = [{ prop: 'accountNumber' }, { prop: 'accountType' }, { prop: 'accountId' }, { prop: 'user' }
     , { prop: 'datefield' }, { prop: 'amount' }];
 

    myForm: FormGroup;
    
    fromDate!: string;
    toDate!: string;
    startAmount!: number;
    endAmount!: number;
    accountId!: number;
    



    constructor(private userService: UserService,private authenticationService: AuthenticationService,
        private http: HttpClient,private fb: FormBuilder) {
        this.user = <User>this.authenticationService.userValue;
        this.role = this.user.role == 'ROLE_USER' ? 'USER' : 'ADMIN';
        this.myForm = this.fb.group({
            accountId: [''],
            fromDate: [''],
            toDate: [''],
            startAmount: [''],
            endAmount: ['']
          });
          
    }

    ngOnInit() {
        this.loading = true;

        if(this.user.role == 'ROLE_USER') {
            this.userService.getById(this.user.username).subscribe((data: UserAccountStatement[]) => {
                this.loading = false;
                this.rows = data ;
            });
        } else {
            this.getAllApi("","","",0,100000);
        }
    }

    submitForm() {
        if (this.myForm.valid) {
          let startAmount = this.myForm.value.startAmount === '' || this.myForm.value.startAmount == undefined ? '0' : this.myForm.value.startAmount;
          let endAmount = this.myForm.value.endAmount === '' || this.myForm.value.endAmount == undefined ? '100000' : this.myForm.value.endAmount;
          let accountId = this.myForm.value.accountId === '' || this.myForm.value.accountId == undefined ? '' : this.myForm.value.accountId;
          let fromDate = this.myForm.value.fromDate === '' || this.myForm.value.fromDate == undefined ? '' : this.myForm.value.fromDate;
          let toDate = this.myForm.value.toDate === '' || this.myForm.value.toDate == undefined ? '' : this.myForm.value.toDate;
          
          this.getAllApi(accountId,fromDate,toDate,startAmount,endAmount);
        }
      }
    
      resetForm() {
        this.myForm.reset();
      }

    getAllApi(accountId: string,fromDate: string,toDate: string, startAmount: number, endAmount: number) {
        this.userService.getAll(accountId,fromDate,toDate,startAmount,endAmount).subscribe((data: UserAccountStatement[] | undefined) => {
            this.loading = false;
            this.rows = data;
        });
    }

    fromDateFormat(event: any) {
        // Format date string to yyyy-MM-dd with timezone set to UTC
        const date = new Date(event.target.value);
        const formattedDate = date.toISOString().split('T')[0];
        this.fromDate = formattedDate;
      }

      toDateFormat(event: any) {
        // Format date string to yyyy-MM-dd with timezone set to UTC
        const date = new Date(event.target.value);
        const formattedDate = date.toISOString().split('T')[0];
        this.toDate = formattedDate;
      }

      lastThreeMonthForm() {
        this.getAllApi("","","",0,100000);
      }
}