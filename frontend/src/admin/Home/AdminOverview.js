import { Button, ButtonGroup, Container, Form, Row } from "react-bootstrap"
import React, { useEffect, useState } from 'react'
import { Chart } from "react-google-charts";
import orderService from "../../service/order.service";
import Loader from "../../shared/util/Loader/Loader";
import { Download } from "react-bootstrap-icons";
import userService from "../../service/user.service";


function AdminOverview() {
    var mounted = false;
    const [searchResults, setSearchResults] = useState([]);
    const [revenueResults, setrevenueResults] = useState([]);
    const [totalSpending, settotalSpending] = useState();
    const [loading, setLoading] = useState(false);
    

    useEffect(() => {
        if (!mounted) {
            mounted = true;
            setLoading(true)
            
            orderService.getAdminOrders().then(res => {
                if (res.status == 200) {
                    
                    var list = [["Restaurant", "Number of orders"]]
                    
                    Object.entries(res.data).forEach(item => {
                        list = [...list,item]
                    })
                    setSearchResults(list);
                    setLoading(false)
                }
                else
                    console.log(res)

            })
            orderService.getAdminRestaurantRevenue().then(res => {
                if (res.status == 200) {
                    
                    var list = [["Restaurant", "Revenue"]]
                    
                    Object.entries(res.data).forEach(item => {
                        list = [...list,item]
                    })
                    setrevenueResults(list);
                    setLoading(false)
                }
                else
                    console.log(res)

            })
            orderService.getAdminSpending().then(res => {
                if (res.status == 200) {
                    settotalSpending(res.data)
                    setLoading(false)
                }
                else
                    settotalSpending(-1)

            })
  
        }
    }, [])
    
    const options = {
        title: "Number of orders",
    };
    const options2 = {
        title: "Restaurant revenue (KM)",
    };

    const downloadAnnualReport =  ()=> {
        orderService.getAnnualReport().then( res => {
            if(res.status == 200) {
                const url = window.URL.createObjectURL(new Blob([res.data], { type: 'application/pdf' }));
                const link = document.createElement('a');
                link.href = url;
                link.setAttribute('download', 'annual-report.pdf');
                document.body.appendChild(link);
                link.click();
                link.remove();
            }
        })
    }

    const downloadUserAnalysisReport =  ()=> {
        userService.getUserAnalysisReport().then( res => {
            if(res.status == 200) {
                const url = window.URL.createObjectURL(new Blob([res.data], { type: 'application/pdf' }));
                const link = document.createElement('a');
                link.href = url;
                link.setAttribute('download', 'user-analsysis-report.pdf');
                document.body.appendChild(link);
                link.click();
                link.remove();
            }
        })
    }

    
    return (
        <>
        <Loader isOpen={loading} >
            <Container style={{ backgroundColor: "#D9D9D9",  margin: "auto", marginTop: "20px", marginBottom: "20px", width:"100%"}}>
                <h1>Admin overview</h1>
                <ButtonGroup
                style={{
                    float:'right'
                }}>
                <Button style={{
                borderTopLeftRadius: 5,
                borderTopRightRadius: 0,
                borderBottomLeftRadius: 5,
                borderBottomRightRadius: 0,
                border: "#FE724C",
                width: "200px",
              }}
              variant="secondary"
              onClick={downloadAnnualReport}>Annual Report  <Download></Download></Button>
                <Button style={{
                borderTopLeftRadius: 0,
                borderTopRightRadius: 5,
                borderBottomLeftRadius: 0,
                borderBottomRightRadius: 5,
                border: "#FE724C",
                width: "200px",
              }}
              variant="secondary"
              onClick={downloadUserAnalysisReport}
              >User Analysis Reports  <Download></Download></Button>
                </ButtonGroup>
                <hr/>
                <h3>Amount spent to date: {totalSpending} KM</h3>
                <hr/>
                <Chart
                chartType="PieChart"
                data={searchResults}
                options={options}
                width={"100%"}
                height={"400px"}
                />
                <hr/>
                <Chart
                chartType="PieChart"
                data={revenueResults}
                options={options2}
                width={"100%"}
                height={"400px"}
                />
            <hr/>   
            </Container>
        </Loader>
        </>
        
    )
    
}

export default AdminOverview