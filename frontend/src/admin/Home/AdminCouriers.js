import React, { useEffect, useState } from 'react'
import restaurantService from '../../service/restaurant.service';
import { Container ,Button, Modal } from 'react-bootstrap';
import Loader from '../../shared/util/Loader/Loader';
import BootstrapTable from "react-bootstrap-table-next";
import paginationFactory from "react-bootstrap-table2-paginator";
//import "bootstrap/dist/css/bootstrap.css";
import "react-bootstrap-table-next/dist/react-bootstrap-table2.min.css";
import { Search, Add } from '@mui/icons-material'
import userService from '../../service/user.service';
import CourierModal from './CourierModal';


function AdminCouriers() {
    var mounted = false;
    const [searchResults, setSearchResults] = useState();
    const [loading, setLoading] = useState(false);
    const [chosenCourier, setchosenCourier] = useState({firstname:"", lastname:"", username:"", email:"", phoneNumber:""});
    const [openReportDialog, setOpenReportDialog] = useState(false);
    const [showModal, setshowModal] = useState(false);

    useEffect(() => {
        if (!mounted) {

            mounted = true;
            setLoading(true);
            userService.getAllCouriers().then(mng => {
                //setLoading(false)
                if (mng.status == 200){
                    setSearchResults(mng.data)
                    console.log(mng.data)
                    setLoading(false)
                }
            })


        }
    }, [])

    const addNewCourier = () => {
        setOpenReportDialog(true);
        //console.log("Kreiranje couriera");
        //console.log(openReportDialog);
    }

    const columns = [
        {
          dataField: "firstname",
          text: "Name",
          sort: true
        },
        {
          dataField: "lastname",
          text: "Surname",
          sort: true
        },
        {
          dataField: "email",
          text: "Email",
          sort:true
        },
        {
            dataField: "username",
            text: "Username",
            sort:true
        },
        {
            dataField: "address",
            text: "Address",
        },
        {
            dataField: "phoneNumber",
            text: "Phone number"
        },
      ];
      const closeModal=()=>{
        setshowModal(false)
      }


        const openModal=()=>{
            setshowModal(true)
        }

      const rowEvents = {
        onClick: (e, row, rowIndex) => {
          //console.log(row) // ovo je objekat u tom redu
          setchosenCourier(row)
          setshowModal(true);
        }
      };


      const deleteCourier = () => {
        setSearchResults((current) =>
            current.filter((rest) => rest.id !== chosenCourier.id)
        );
        userService.deleteUser(chosenCourier.id).then( res => {
            console.log(res);
        })
        setshowModal(false);
      }


    return (
        <>
            <Loader isOpen={loading} >
                {searchResults ?
                    <Container style={{ backgroundColor: "#D9D9D9",  margin: "auto", marginTop: "20px", marginBottom: "20px", width:"100%"}}>
                        <Container style={{ backgroundColor: "#F5F5F4", borderRadius: "5px",borderBottomRightRadius:0,borderBottomLeftRadius:0, width: "100%", minWidth: "35rem", marginBottom: 0, }}>
                            <h2 style={{ textAlign: "start", float: "left", margin:0 }}>{"All couriers"}</h2>
                            <Container style={{ display: "flex", justifyContent: "flex-end", alignItems: "flex-end", backgroundColor: "#F5F5F4", marginBottom: 0, marginRight: 0,padding:0, paddingBottom:10 }}>
                                <Button style={{ clear: "left", textAlign: "center", width: "fit-content",  }} class="rounded" onClick={addNewCourier}>Add courier <Add ></Add></Button>
                                <CourierModal
                                show={openReportDialog}
                                setShow={setOpenReportDialog}
                                couriers={searchResults}
                                setCouriers={setSearchResults}
                                ></CourierModal>
                            </Container>
                                <hr style={{ clear: "left", margin:0 }}></hr>
                        </Container>
                        <BootstrapTable
                            bootstrap4
                            striped 
                            hover 
                            rowEvents={ rowEvents }
                            keyField="id"
                            data={searchResults}
                            columns={columns}
                            pagination={paginationFactory({ sizePerPage: 5 })}
                        />
                        <Modal show={showModal} onHide={closeModal}>
                            <Modal.Header closeButton>
                            <Modal.Title>Overview: {chosenCourier.username}</Modal.Title>
                            </Modal.Header>
                            
                            <Modal.Body>
                                Full name: {chosenCourier.firstname}  {chosenCourier.lastname}
                                <br></br>
                                Email: {chosenCourier.email}
                                <br></br>
                                Phone number: {chosenCourier.phoneNumber}
                                <br></br>
                                
                            </Modal.Body>
                            <Modal.Footer>
                            <Button variant="secondary" onClick={closeModal}>
                                Cancel
                            </Button>
                            
                            </Modal.Footer>
                        </Modal>
                        
                    </Container> : <></>}
            </Loader>
        </>
    )
}

export default AdminCouriers