import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ReportService } from '../../services/report.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './reports.component.html',
  styleUrl: './reports.component.css'
})
export class ReportsComponent implements OnInit {

  downloadingId: number | null = null;
  reportHistory: any[] = [];

  filterForm = {
    initalDate: '',
    finalDate: '',
    status: '',
    deliveryMethod: '',
    custumerName: '',
    productName: '',
    productType: ''
  };

  statusOptions = [
    { value: 'AWAITING_ANALYSIS', label: 'Aguardando Análise' },
    { value: 'AWAITING_PAYMENT', label: 'Aguardando Pagamento' },
    { value: 'PAID', label: 'Pago' },
    { value: 'IN_DELIVERY', label: 'Em entrega' },
    { value: 'DELIVERED', label: 'Entregue' },
    { value: 'CANCELED', label: 'Cancelado' }
  ];

  deliveryOptions = [
    { value: 'PICKUP', label: 'Retirada' },
    { value: 'DELIVERY', label: 'Entrega' }
  ];

  productTypeOptions = [
    { value: 'FLOR_CORTE', label: 'Flor de corte' },
    { value: 'FLOR_MUDA', label: 'FLor muda' },
    { value: 'ARVORE_FRUTIFERA', label: 'Arvore Frutifera' },
    { value: 'ARVORE_ORNAMENTAL', label: 'Arvore Ornamental' },
    { value: 'ARBUSTO', label: 'Abusto' },
    { value: 'VASO', label: 'Vaso' },
    { value: 'BUQUE', label: 'Buque' },
    { value: 'CESTA', label: 'Cesta' },
    { value: 'OUTRO', label: 'Outro' },
  ];

  constructor(private reportService: ReportService) { }

  ngOnInit(): void {
    this.loadHistory();
  }

  loadHistory(): void {
    this.reportService.getReportHistory().subscribe({
      next: (data) => this.reportHistory = data.sort((a, b) => b.id - a.id),
      error: (err: HttpErrorResponse) => console.error('Erro ao carregar histórico', err)
    });
  }

  generateReport(): void {
    if (!this.filterForm.initalDate || !this.filterForm.finalDate) {
      alert('Por favor, selecione as datas de início e fim.');
      return;
    }

    const payload: any = {
      initalDate: `${this.filterForm.initalDate}T00:00:00`,
      finalDate: `${this.filterForm.finalDate}T23:59:59`
    };

    if (this.filterForm.status) payload.status = this.filterForm.status;
    if (this.filterForm.deliveryMethod) payload.deliveryMethod = this.filterForm.deliveryMethod;
    if (this.filterForm.productType) payload.productType = this.filterForm.productType;
    if (this.filterForm.custumerName) payload.custumerName = this.filterForm.custumerName;
    if (this.filterForm.productName) payload.productName = this.filterForm.productName;

    this.reportService.generateReport(payload).subscribe({
      next: () => {
        this.cleanFilters();
        this.loadHistory();
      },
      error: (err: HttpErrorResponse) => {
        console.error('Erro ao gerar relatório', err);
        alert('Ocorreu um erro ao gerar o relatório. Tente novamente.');
      }
    });
  }

  cleanFilters(): void {
    this.filterForm = {
      initalDate: '', finalDate: '', status: '', deliveryMethod: '',
      custumerName: '', productName: '', productType: ''
    };
  }


  downloadPdf(id: number): void {

    this.downloadingId = id;

    this.reportService.downloadPdf(id).subscribe({
      next: (blob: Blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `Relatorio_Espaco_Verde_${id}.pdf`;
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
        a.remove();
        
        this.downloadingId = null;
      },
      error: (err) => {
        this.downloadingId = null;
        console.error('Erro no download', err);
        alert('Erro ao tentar baixar o arquivo PDF.');
      }
    });
  }

}
