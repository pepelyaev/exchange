import React, {Component} from 'react'
import axios from 'axios';

let flag = true;

class Table extends Component {
    constructor(props) {
        super(props)
        this.state = {
            tickers: [
                "BAC", "BEN", "BTI", "CHL", "CMA", "COP", "CSCO", "CVX", "EXC", "GILD", "HCSG", "HFC", "HPE",
                "IBM", "INTC", "IVZ", "KIM", "MET", "MOMO", "PBCT", "PRU", "T", "TDS", "UNM"
            ],
            rows: {}
        }
        if (flag) {
            flag = false;
            axios.get('http://localhost:8080/app/request/ticker=' + this.state.tickers.join(","))
                .then(response => {
                    response.data.map((row, index) => {
                        this.state.rows[row.ticker] = row;
                    });
                });
        }
    }

    renderTableData() {
        return Object.keys(this.state.rows).map((ticker, index) => {
            let row = this.state.rows[ticker];
            return (
                <tr key={index}>
                    <td key={ticker}>{ticker}</td>
                    <td key={row.dateBue}>{row.dateBue}</td>
                    <td key={row.dateClose}>{row.dateClose}</td>
                    <td key={row.datePay}>{row.datePay}</td>
                    <td key={row.value}>{row.value}</td>
                    <td key={row.recommended}>{row.recommended ? 'Yes' : 'No'}</td>
                    <td key={row.ps}>{row.ps}</td>
                    <td key={row.payout}>{row.payout}</td>
                    <td key={row.operMargin}>{row.operMargin}</td>
                    <td key={row.epsNext5y}>{row.epsNext5y}</td>
                    <td key={row.epsNext1y}>{row.epsNext1y}</td>
                    <td key={row.pb}>{row.pb}</td>
                    <td key={row.targetPrice}>{row.targetPrice}</td>
                    <td key={row.pe}>{row.pe}</td>
                    <td key={row.profitMargin}>{row.profitMargin}</td>
                    <td key={row.fpe}>{row.fpe}</td>
                    <td key={row.grossMargin}>{row.grossMargin}</td>
                    <td key={row.epsThis1y}>{row.epsThis1y}</td>
                    <td key={row.debtEq}>{row.debtEq}</td>
                    <td key={row.epsPast5y}>{row.epsPast5y}</td>
                    <td key={row.ltDebtEq}>{row.ltDebtEq}</td>
                    <td>
                        <button>UPDATE</button>
                    </td>
                </tr>
            )
        })
    }

    render() {
        return (
            <div>
                <h1 id='title'>React Dynamic Table</h1>
                <table id='rows'>
                    <tbody>
                    {this.renderTableData()}
                    </tbody>
                </table>
            </div>
        )
    }
}

export default Table
