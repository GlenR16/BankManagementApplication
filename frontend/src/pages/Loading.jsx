export default function Loading() {
	return (
		<div className="flex-grow-1 d-flex align-items-center justify-content-center">
			<div className="container">
				<div className="row flex-lg-row-reverse align-items-center justify-content-center text-center p-3">
					<h1 className="display-5 fw-bold">Loading</h1>
					<div className="spinner-border" role="status" style={{"width":"4rem","height":"4rem"}}>
						<span className="visually-hidden">Loading...</span>
					</div>
				</div>
			</div>
		</div>
	);
}
